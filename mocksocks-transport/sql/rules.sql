CREATE TABLE Rules(
  Type varchar(20) PRIMARY KEY,
  Content varchar(5000),
);

CREATE TABLE Message(
  ID INT PRIMARY KEY auto_increment,
  Body blob
);

CREATE TABLE Exchange(
  ID INT PRIMARY KEY auto_increment,
  Connection

);