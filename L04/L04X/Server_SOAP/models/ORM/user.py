from database.mariadb_connection import Base
from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from models.ORM.user_role import user_role

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    username = Column(String, unique=True, nullable=False)
    password = Column(String, nullable=False)
    roles = relationship("Role", secondary=user_role)

    def __init__(self, username, password):
        self.username = username
        self.password = password

    def set_password(self, password):
        self.password = password