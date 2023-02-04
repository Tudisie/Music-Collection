from models.ORM.role import Role
from database.mariadb_connection import session

def create_role(name):
    try:
        session.add(Role(name))
        session.commit()
    except Exception as ex:
        print(f"Failed to add new role: {ex}")


def delete_role(name):
    try:
        role = session.query(Role).filter(Role.name == name).first()
        session.delete(role)
        session.commit()
    except Exception as ex:
        print(f"Invalid to delete role: {ex}")


def get_roles():
    roles = session.query(Role).all()
    return roles
