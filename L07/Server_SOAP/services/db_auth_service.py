# pip install lxml spyne python-jose
import spyne.error
from spyne import Application, rpc, ServiceBase, Boolean, String, Integer, Array
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
from repositories.role_repository import *
from repositories.user_repository import *
from models.DTO.user_roles import UserRoles
from jose import jws
import uuid
import json
from datetime import timezone, datetime, timedelta


def check_claims(jws_obj):
    if jws_obj == None:
        raise spyne.error.InvalidCredentialsError()

    claims = json.loads(jws.verify(jws_obj, 'secret', algorithms='HS256'))
    user_id = claims['sub']

    for blackjws in blacklist:
        if jws_obj == blackjws:
            print("blacklisted jws tried to authorize")
            raise spyne.error.InvalidCredentialsError()

    if claims['exp'] < datetime.timestamp(datetime.now(tz=timezone.utc)):
        blacklist.append(jws_obj)
        raise spyne.error.InvalidCredentialsError()

    user = get_user_by_id(user_id)
    role_list = []
    if user != None:
        for role in user.roles:
            role_list.append(str(role.name))
    return UserRoles(user.username, role_list, user.email, user.full_name, user.age)

class CorsService(ServiceBase):
    origin = '*'

def _on_method_return_object(ctx):
    ctx.transport.resp_headers['Access-Control-Allow-Origin'] = '*'

CorsService.event_manager.add_listener('method_return_object', _on_method_return_object)

class DbAuthService(CorsService):
    # Roles
    @rpc(String, _returns=Array(String, wrapped=False))
    def get_all_roles(self, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles:
            raise spyne.error.InvalidCredentialsError()
        roles = get_roles()
        role_list = []
        for role in roles:
            role_list.append(role.name)
        return role_list

    @rpc(String, _returns=Array(String, wrapped=False))
    def get_roles(self, jws_obj):
        user_roles = check_claims(jws_obj)
        return user_roles.roles


    @rpc(String, String)
    def create_role(self, name, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles:
            raise spyne.error.InvalidCredentialsError()
        create_role(name)

    @rpc(String, String)
    def delete_role(self, name, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles:
            raise spyne.error.InvalidCredentialsError()
        delete_role(name)

    # Users

    @rpc(String, _returns=Array(UserRoles, wrapped=False))
    def get_all_users_with_roles(self, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles:
            raise spyne.error.InvalidCredentialsError()
        users = get_users()
        users_list = []
        for user in users:
            role_list = []
            for role in user.roles:
                role_list.append(role.name)
            users_list.append(UserRoles(user.username, role_list, user.email, user.full_name, user.age))

        return users_list

    @rpc(String, String, _returns=UserRoles)
    def get_user_with_roles(self, username, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles and username != user_roles.username:
            raise spyne.error.InvalidCredentialsError()
        user = get_user(username)
        if user == None:
            raise Exception("Exception: User not found!")
        role_list = []
        for role in user.roles:
            role_list.append(role.name)

        return UserRoles(username, role_list, user.email, user.full_name, user.age)

    @rpc(String, _returns=String)
    def get_username_by_jws(self, jws_obj):
        user_roles = check_claims(jws_obj)
        return user_roles.username

    @rpc(String, _returns=Integer)
    def get_user_id_by_jws(self, jws_obj):
        user_roles = check_claims(jws_obj)
        user = get_user(user_roles.username)
        return user.id

    @rpc(String, String, String, String, Integer)
    def create_user(self, username, password, email, full_name, age):
        create_user(username, password, email, full_name, age)

    @rpc(String, String)
    def delete_user(self, username, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles:
            raise spyne.error.InvalidCredentialsError()
        delete_user(username)

    @rpc(String, String, String, _returns=Boolean)
    def change_password(self, username, new_password, jws_obj):
        user_roles = check_claims(jws_obj)
        if username != user_roles.username:
            raise spyne.error.InvalidCredentialsError()
        return change_password(username, new_password)

    @rpc(String, Array(String, wrapped=False), String)
    def assign_roles_to_user(self, username, role_list, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles:
            raise spyne.error.InvalidCredentialsError()
        assign_roles_to_user(username, role_list)

    @rpc(String, Array(String, wrapped=False), String)
    def remove_roles_from_user(self, username, role_list, jws_obj):
        user_roles = check_claims(jws_obj)
        if "administrator aplicatie" not in user_roles.roles:
            raise spyne.error.InvalidCredentialsError()
        remove_roles_from_user(username, role_list)

    # Security

    @rpc(String, String, _returns=String)
    def login(self, username, password):
        user = get_user(username)
        expiration_date = datetime.now(tz=timezone.utc) + timedelta(seconds=1800)
        if user != None:
            if user.password == password:
                roles = []
                for role in user.roles:
                    roles.append(role.name)
                claims = {
                    "iss": "db.auth.service",
                    "sub": user.id,
                    "exp": datetime.timestamp(expiration_date),
                    "roles": roles, # for REST API - song-collection validation
                    "jti": str(uuid.uuid4())
                }
                return jws.sign(claims, 'secret', algorithm='HS256')
        raise spyne.error.InvalidCredentialsError()

    @rpc(String, _returns=UserRoles)
    def authorize(self, jws_obj):
        return check_claims(jws_obj)

    @rpc(String)
    def logout(self, jws_obj):
        blacklist.append(jws_obj)

    # regularly check the blacklist and remove the old entries to save space - not used
    def clean_blacklist(self):
        for blackjws in blacklist:
            claims = json.loads(jws.verify(blackjws, 'secret', algorithms='HS256'))
            # older than 24 hours ago
            if claims['exp'] < (datetime.timestamp(datetime.now(tz=timezone.utc) - timedelta(seconds=24*3600))):
                blacklist.remove(blackjws)


application = Application([DbAuthService], 'services.db-auth.soap', in_protocol=Soap11(validator='lxml'), out_protocol=Soap11())
wsgi_application = WsgiApplication(application)

blacklist = []

if __name__ == '__main__':
    import logging
    from wsgiref.simple_server import make_server

    logging.basicConfig(level=logging.INFO)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.INFO)
    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://127.0.0.1:8000/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()