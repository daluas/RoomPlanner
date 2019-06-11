set mode PostgreSQL;

create schema if not exists "public";

drop table if exists roles_rights;
drop table if exists users_rights;
drop table if exists users_roles;
drop table if exists rights;
drop table if exists roles;
drop table if exists users;
drop sequence if exists seq_user_id;
drop sequence if exists seq_role_id;
drop sequence if exists seq_right_id;

create table users(
  id serial primary key,
  birth_date date not null
);
create sequence seq_user_id start with 1 increment BY 1;

alter table users drop column birth_date;

alter table users add column email varchar(100) not null;
alter table users add constraint users_email_nn unique(email);
alter table users add column password varchar(50) not null;
alter table users add column type varchar(50) not null;
alter table users add column first_name varchar(50);
alter table users add column last_name varchar(50);
alter table users add column room_name varchar(50);
alter table users add column floor int;
alter table users add column max_persons int;

alter table users alter column id set default nextval('seq_user_id');

create sequence seq_role_id start with 1 increment BY 1;
create sequence seq_right_id start with 1 increment BY 1;

create table roles
(
	id serial not null primary key,
	name varchar(50) not null

);

alter table roles add constraint roles_name_nn unique(name);

alter table roles alter column id set default nextval('seq_role_id');

create table rights
(
	id serial not null primary key,
	name varchar(50) not null

);

alter table rights add constraint rights_name_nn unique(name);

alter table rights alter column id set default nextval('seq_right_id');

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

insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons) values('sghitun@yahoo.com', 'sajhsar2A%','PERSON', 'Stefania', 'Ghitun', null, null, null);
insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons) values('wonderland@yahoo.com', '4wonD2C%','ROOM', null, null, 'Wonderland', 5, 14);
insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons) values('westeros@yahoo.com', '4westAD8%','ROOM', null, null, 'Westeros', 8, 21);
INSERT INTO roles(name) VALUES ('user');
INSERT INTO users_roles VALUES(1,1);


insert into roles (name) values ('room');
insert into roles (name) values ('person');


insert into users_roles (user_id, role_id) values (1, 3);
insert into users_roles (user_id, role_id) values (2, 2);


insert into rights (name) values ('view');
insert into rights (name) values ('write');
insert into rights (name) values ('delete');

insert into roles_rights (role_id, right_id) values (2,1);
insert into roles_rights (role_id, right_id) values (3,1);
insert into roles_rights (role_id, right_id) values (3,2);