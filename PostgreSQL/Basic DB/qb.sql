create function trigf1() returns trigger AS
$$
begin
IF ((select c.population from city as c where c.cname = NEW.cname) < (new.casualties))
then
	begin
		raise notice 'Error: Number of predicted casulaties is greater or equal to population size';
		return null;
	end;
else 
	return new;
end if;
end;
$$language plpgsql;

create trigger T1
before insert on Prediction
for each row
execute procedure trigf1();