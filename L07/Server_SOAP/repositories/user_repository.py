import spyne.error

from models.ORM.role import Role
from models.ORM.user import User
from database.mariadb_connection import session

def get_users():
    users = session.query(User).all()
    return users

def get_user(username):
    try:
        user = session.query(User).filter(User.username == username).first()
        return user
    except Exception as ex:
        print(f"Couldn't get user {username}: {ex}")

def get_user_by_id(id):
    try:
        user = session.query(User).filter(User.id == id).first()
        return user
    except Exception as ex:
        print(f"Couldn't get user {id}: {ex}")

def create_user(username, password, email, full_name, age):
    user = User(username, password, email, full_name, age)
    try:
        session.add(user)
        session.commit()
        assign_roles_to_user(username,  ['client'])
    except Exception as ex:
        print(f"Failed to add user: {ex}")
        session.rollback()
        raise spyne.error.Fault()
    return user

def delete_user(username):
    try:
        user = session.query(User).filter(User.username == username).first()
        session.delete(user)
        session.commit()
    except Exception as ex:
        print(f"Invalid username: {ex}")

def change_password(username, new_password):
    try:
        user = session.query(User).filter(User.username == username).first()
        user.set_password(new_password)
        session.commit()
        return True
    except Exception as ex:
        print(f"Couldn't change password of user {username}: {ex}")
        return False

def assign_roles_to_user(username, user_roles):
    user = session.query(User).filter(User.username == username).first()
    roles = session.query(Role).all()
    for user_role in user_roles:
        found = False
        for role in roles:
            if user_role == role.name:
                found = True
                user.roles.append(role)
                break
        if found == False:
            raise spyne.error.Fault()
    session.commit()


def remove_roles_from_user(username, user_roles):
    user = session.query(User).filter(User.username == username).first()
    roles = session.query(Role).all()
    for user_role in user_roles:
        # check if role exists
        found = False
        for role in roles:
            if user_role == role.name:
                found = True
        if found == False:
            raise spyne.error.Fault()

        for role in user.roles:
            if user_role == role.name:
                user.roles.remove(role)
    session.commit()