# tutorial: https://youtu.be/47i-jzrrIGQ
from sqlalchemy import Column, Integer, Table, ForeignKey
from database.mariadb_connection import Base

# association table (many-to-many)
user_role = Table(
    'user-roles', Base.metadata,
    Column('user_id', Integer, ForeignKey('users.id')),
    Column('role_id', Integer, ForeignKey('roles.id')),
    extend_existing=True
)
