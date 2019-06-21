create table floors
(
    id serial primary key not null,
    floor int not null,
    room_id int not null check(room_id > 0) references users(id)
);

create sequence seq_floor_id start with 1 increment BY 1;

alter table floors alter column id set default nextval('seq_floor_id');

alter table users rename column floor to floor_id;

update users set floor_id = 1 where id = 2;
update users set floor_id = 2 where id = 3;
update users set floor_id = 3 where id = 4;

insert into floors (floor,room_id) values (5,2);
insert into floors (floor,room_id) values (8,3);
insert into floors (floor,room_id) values (4,4);

alter table users add constraint floor_id_fk foreign key (floor_id) references floors (id);



