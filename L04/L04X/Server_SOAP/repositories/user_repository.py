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

def create_user(username, password):
    user = User(username, password)
    try:
        session.add(user)
        session.commit()
    except Exception as ex:
        print(f"Failed to add user: {ex}")
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
    except Exception as ex:
        print(f"Couldn't change password of user {username}: {ex}")

def assign_roles_to_user(username, user_roles):
    try:
        user = session.query(User).filter(User.username == username).first()
        roles = session.query(Role).all()
        for user_role in user_roles:
            for role in roles:
                if user_role == role.name:
                    user.roles.append(role)
                    break
        session.commit()
    except Exception as ex:
        print(f"Failed to assign roles to user - {ex}")
