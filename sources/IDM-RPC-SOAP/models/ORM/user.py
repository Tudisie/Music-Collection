from database.mariadb_connection import Base
from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from models.ORM.user_role import user_role

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    username = Column(String, unique=True, nullable=False)
    password = Column(String, nullable=False)
    email = Column(String)
    full_name = Column(String)
    age = Column(Integer)
    roles = relationship("Role", secondary=user_role)

    def __init__(self, username, password, email, full_name, age):
        self.username = username
        self.password = password
        self.email = email
        self.full_name = full_name
        self.age = age

    def set_password(self, password):
        self.password = password