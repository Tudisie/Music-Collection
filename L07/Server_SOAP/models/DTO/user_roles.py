from spyne import ComplexModel, String, Array, Integer


class UserRoles(ComplexModel):
    username = String
    email = String
    full_name = String
    age = Integer
    roles = Array(String, wrapped=False)

    def __init__(self, username, roles, email, full_name, age):
        super(UserRoles, self).__init__()
        self.username = username
        self.email = email
        self.full_name = full_name
        self.age = age
        self.roles = roles