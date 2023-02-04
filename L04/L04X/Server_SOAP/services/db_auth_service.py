# pip install lxml spyne
from spyne import Application, rpc, ServiceBase, String, Boolean, Array
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
from repositories.role_repository import *
from repositories.user_repository import *
from models.DTO.user_roles import UserRoles

class DbAuthService(ServiceBase):

    # Roles
    @rpc(_returns=Array(String, wrapped=False))
    def get_roles(self):
        roles = get_roles()
        role_list = []
        for role in roles:
            role_list.append(role.name)
        return role_list

    @rpc(String)
    def create_role(self, name):
        create_role(name)

    @rpc(String)
    def delete_role(self, name):
        delete_role(name)

    # Users

    @rpc(_returns=Array(UserRoles, wrapped=False))
    def get_all_users_with_roles(self):
        users = get_users()
        users_list = []
        for user in users:
            role_list = []
            for role in user.roles:
                role_list.append(role.name)
            users_list.append(UserRoles(user.username, role_list))

        return users_list

    @rpc(String, _returns=UserRoles)
    def get_user_with_roles(self, username):
        try:
            user = get_user(username)
            if user == None:
                raise Exception("Exception: User not found!")
            role_list = []
            for role in user.roles:
                role_list.append(role.name)

            return UserRoles(username, role_list)
        except Exception as ex:
            print(ex)

    @rpc(String, String)
    def create_user(self, username, password):
        create_user(username, password)

    @rpc(String)
    def delete_user(self, username):
        delete_user(username)

    @rpc(String, String)
    def change_password(self, username, new_password):
        change_password(username, new_password)

    @rpc(String, Array(String, wrapped=False))
    def assign_roles_to_user(self, username, role_list):
        assign_roles_to_user(username, role_list)

    # Security

    @rpc(String, String, _returns=Array(String, wrapped=False))
    def login(self, username, password):
        user = get_user(username)
        if user != None:
            if user.password == password:
                role_list = []
                for role in user.roles:
                    role_list.append(role.name)
                return role_list

    @rpc(String, String, _returns=Boolean)
    def authorize(self, username, role_name):
        user = get_user(username)
        if user != None:
            for role in user.roles:
                if role.name == role_name:
                    return True
        return False

application = Application([DbAuthService], 'services.db-auth.soap', in_protocol=Soap11(validator='lxml'), out_protocol=Soap11())

wsgi_application = WsgiApplication(application)

if __name__ == '__main__':
    import logging
    from wsgiref.simple_server import make_server

    logging.basicConfig(level=logging.INFO)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.INFO)
    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://127.0.0.1:8000/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()