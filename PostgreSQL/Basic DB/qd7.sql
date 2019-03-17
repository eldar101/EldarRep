select p.*, m1.provider as provider_1, m2.provider as provider_2,
         p.casualties * (1 - m1.percent / 100.0) * (1 - m2.percent / 100.0) as net_casualties,
         (m1.mcost + m2.mcost) as total_cost
  from measures m1 join
       measures m2
       on m1.etype = m2.etype and m1.provider < m2.provider join
       prediction p
       on m1.etype = p.etype;

       with pairs as (
      select p.*, m1.provider as provider_1, m2.provider as provider_2,
             p.casualties * (1 - m1.percent / 100.0) * (1 - m2.percent / 100.0) as net_casualties,
             (m1.mcost + m2.mcost) as total_cost
      from measures m1 join
           measures m2
           on m1.etype = m2.etype and m1.provider < m2.provider join
           prediction p
           on m1.etype = p.etype
    )
select cname, etype, provider_1,provider_2
from pairs p
where p.total_cost < 1000000 and
      p.net_casualties = (select min(p2.net_casualties)
                          from pairs p2
                          where p2.cname = p.cname and p2.etype = p.etype and
                                p2.total_cost < 1000000
                         );