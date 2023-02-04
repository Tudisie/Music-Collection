# Tutorial Client SOAP: https://github.com/suds-community/suds#readme
from suds.client import Client

url = 'http://localhost:8000/?wsdl'
client = Client(url)

# Testing Roles
print(client.service.get_roles())
client.service.create_role("Boss")
print(client.service.get_roles())
client.service.delete_role("Boss")
print(client.service.get_roles())

# Testing Users

print(client.service.get_user_with_roles("Tudisie"))

client.service.create_user("Anastasia", "nudalanimeni")
client.service.assign_roles_to_user("Anastasia",["artist", "client"])

print(client.service.get_all_users_with_roles())

client.service.delete_user("Anastasia")

# Security

print(client.service.login("", "Passwd123"))
print(client.service.login("Tudisie", "Passwd123"))

print(client.service.authorize("Tudisie", "artist"))
print(client.service.authorize("Tudisie", "content manager"))



