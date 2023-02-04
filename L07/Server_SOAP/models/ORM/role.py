from database.mariadb_connection import Base
from sqlalchemy import Column, Integer, String

class Role(Base):
    __tablename__ = 'roles'

    id = Column(Integer, primary_key=True)
    name = Column(String, unique=True, nullable=False)

    def __init__(self, name):
        self.name = name