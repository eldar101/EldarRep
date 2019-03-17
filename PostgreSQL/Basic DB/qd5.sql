with s as
(
select cname, sum(casualties),population,sum(casualties)/population::float safety
from prediction
natural join city
group by cname,population)


select cname
from prediction
natural join city
group by cname, population
having sum(casualties)/population::float = (select min(safety)
from s )