create table floors
(
    id serial primary key not null,
    floor int not null
);

create sequence seq_floor_id start with 1 increment BY 1;

alter table floors alter column id set default nextval('seq_floor_id');

alter table users rename column floor to floor_id;

update users set floor_id = 1 where id = 2;
update users set floor_id = 2 where id = 3;
update users set floor_id = 3 where id = 4;

insert into floors (floor) values (5);
insert into floors (floor) values (8);
insert into floors (floor) values (4);

alter table users add constraint floor_id_fk foreign key (floor_id) references floors (id);



