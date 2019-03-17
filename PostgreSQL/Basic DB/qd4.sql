with d1 as (select cname, count(dyear) as number_of_disasters
from (select *
from disaster
where dyear >= date_part('year', current_date) -50) as d
group by cname
)

select cname
from (select *
from disaster
where dyear >= date_part('year', current_date) -50) as d
group by cname
having count(dyear) = (select max(number_of_disasters)
from d1)
