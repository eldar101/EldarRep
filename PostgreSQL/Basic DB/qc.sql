INSERT INTO Event (etype, description)
VALUES ('volcano', 'lava, ash, rock and gasses eruption'), ('earthquake', 'from 5 in Richter scale'), 
('flood', 'overflowing of a large amount of water over what is normally dry land'),
('fire', 'burning that produces flames that send out heat and light and sometimes smoke'),
('tornado', 'funnel-shaped vortex of violently rotating winds advancing beneath a large
storm system');

INSERT INTO City (cname, country, population)
VALUES ('Kagoshima', 'Japan', 700000), ('Naples', 'Italy', 3000000), ('Pasto', 'Colombia',450000),
('Hilo', 'Hawaii', 43000), ('Tsfat', 'Israel', 3600);

INSERT INTO Disaster (cname, dyear, etype, casualties)
VALUES ('Kagoshima', 1915, 'volcano', 100),('Kagoshima', 1973, 'volcano', 50),
('Kagoshima', 2017, 'volcano', 20),('Kagoshima', 1993, 'flood', 2600), ('Kagoshima', 1914, 'earthquake',35),
('Naples', 1906, 'volcano', 50),('Naples', 1944, 'volcano', 35), ('Naples', 1979, 'volcano', 50),
('Naples', 1998, 'flood', 200),('Pasto', 1988, 'volcano', 30),('Pasto', 1993, 'volcano', 45),('Pasto', 2002, 'volcano', 15),
('Pasto', 2008, 'volcano', 30),('Pasto', 2010, 'volcano', 30),('Pasto', 2018, 'flood', 15)
,('Hilo', 1903, 'volcano', 500),('Hilo', 1914, 'volcano', 300),('Hilo', 1926, 'volcano', 20),
('Hilo', 1984, 'volcano',50),('Hilo', 2015, 'tornado', 50),('Hilo', 2011, 'tornado', 70),
('Hilo', 2002, 'tornado', 5),('Hilo', 1989, 'tornado', 50), ('Hilo', 1971, 'tornado', 85),
('Tsfat', 1837, 'earthquake', 5000);

INSERT INTO Prediction (cname, etype, casualties)
VALUES ('Naples', 'volcano', 50), ('Pasto', 'volcano', 500), ('Pasto', 'tornado', 200);

INSERT INTO Measures (etype, provider, mcost, percent)
VALUES ('volcano', 'fire department', 500000, 85), ('volcano', 'police', 200000, 70), ('flood','fire department', 1000000, 60);


