# Tutorial Client SOAP: https://github.com/suds-community/suds#readme
from suds.client import Client

url = 'http://localhost:8000/?wsdl'
client = Client(url)

try:
    jws_obj = client.service.create_user("asdasd", "aaaaaaaaaaa", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkYi5hdXRoLnNlcnZpY2UiLCJzdWIiOjIsImV4cCI6MTY3MzU2OTkwOC4yODY5MDIsImp0aSI6ImFkZTJkNDFiLWQ5ZTItNDAyMC1hY2U3LWE4N2YyNTBmNTcwYiJ9.WkEXD8uCq-pdFAlbEM0mR_lsVlT2_PuaIE-oYqKhUZc")
    print("JWS: " + jws_obj)

    rights = client.service.authorize(jws_obj)
    print("Authorize - roles: " + str(rights["roles"]))

    #client.service.change_password("Doesn't Exist", "Parola123", jws_obj) # exception raised: Not your account

    client.service.assign_roles_to_user("Tudisie", ["artist"], jws_obj)

    users_list = client.service.get_all_users_with_roles(jws_obj)
    print("All users:")
    for user in users_list:
        print(f"{user.username}: {user.roles}")

    print(client.service.get_user_id_by_jws(jws_obj))

    client.service.logout(jws_obj)
    # client.service.authorize(jws_obj) # exception raised: logout invalidated the token
except Exception as e:
    print(e)

