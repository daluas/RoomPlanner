create table reservations
(
	id serial not null primary key,
	start_date timestamp not null,
	end_date timestamp not null,
	description varchar(100),
	user_id int not null check(user_id > 0) references users(id)
);

create sequence seq_reservation_id start with 1 increment BY 1;
alter table reservations alter column id set default nextval('seq_reservation_id');