select d1.cname, d1.dyear, d1.etype from disaster as d1
where d1.dyear >= all(select d2.dyear from disaster as d2 where d2.dyear < (d1.dyear+100) and d2.cname = d1.cname)
and d1.dyear < date_part('year', current_date)-100;