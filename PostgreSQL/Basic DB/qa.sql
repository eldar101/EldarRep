CREATE TABLE Event
(
  etype varchar(15),
  description varchar(100),
  PRIMARY KEY (etype)
);

 CREATE TABLE City
(
  cname varchar(15),
  country varchar(15),
  population int,
  PRIMARY KEY (cname)
);

CREATE TABLE Disaster
(
  cname varchar(15),
  dyear numeric(4,0),
  etype varchar(15),
  casualties int,
  PRIMARY KEY (cname, dyear),
  FOREIGN KEY (cname) REFERENCES City(cname),
  FOREIGN KEY (etype) REFERENCES Event (etype)
);

CREATE TABLE Prediction
(
  cname varchar(15),
  etype varchar(15),
  casualties int,
  PRIMARY KEY (cname, etype),
  FOREIGN KEY (cname) REFERENCES City(cname),
  FOREIGN KEY (etype) REFERENCES Event(etype)
);

CREATE TABLE Measures
(
  etype varchar(15),
  provider varchar(15),
  mcost int,
  percent int,
  PRIMARY KEY (etype, provider),
  FOREIGN KEY (etype) REFERENCES Event (etype)
);
