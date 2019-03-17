select distinct c.country
from disaster as d, city as c
where (d.cname = c.cname) and (d.casualties < 50) and (d.dyear between 1980 and 1990)