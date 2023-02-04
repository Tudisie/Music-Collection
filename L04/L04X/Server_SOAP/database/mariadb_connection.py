# tutorial sqlalchemy cu mariadb: https://mariadb.com/resources/blog/using-sqlalchemy-with-mariadb-connector-python-part-1/

import sqlalchemy
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

print("Connecting to mariadb server...")
engine = sqlalchemy.create_engine('mariadb+mariadbconnector://auth-user:passwdauthuser@192.168.56.10:3306/auth-db')

Base = declarative_base()

Base.metadata.create_all(engine)

Session = sessionmaker()
Session.configure(bind=engine)
session = Session()

print("Connected!")