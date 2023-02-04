from spyne import ComplexModel, String, Array

class UserRoles(ComplexModel):
    username = String
    roles = Array(String, wrapped=False)

    def __init__(self, username, roles):
        super(UserRoles, self).__init__()
        self.username = username
        self.roles = roles