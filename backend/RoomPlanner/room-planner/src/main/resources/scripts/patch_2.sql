alter table users drop column birth_date;

alter table users add column email varchar(100) not null unique;
alter table users add column password varchar(50) not null;
alter table users add column type varchar(50) not null;
alter table users add column first_name varchar(50);
alter table users add column last_name varchar(50);
alter table users add column room_name varchar(50);
alter table users add column floor int;
alter table users add column max_persons int;

create sequence seq_role_id start with 1 increment BY 1;
create sequence seq_right_id start with 1 increment BY 1;

create table roles
(
	id serial not null primary key,
	name varchar(50) not null unique

);

create table rights
(
	id serial not null primary key,
	name varchar(50) not null unique

);

create table users_roles
(
	user_id int not null check(user_id > 0) references users (id),
	role_id int not null check(role_id > 0) references roles (id)
);

create table roles_rights
(
	role_id int not null check(role_id > 0) references roles (id),
	right_id int not null check(right_id > 0) references rights(id)
);


