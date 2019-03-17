select country
from disaster
natural join city
group by cname,city.country
having count(distinct etype) = (select count(etype)
from event)