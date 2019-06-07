create table users
(
	id serial not null primary key,
	email varchar(100) not null unique,
	password varchar(50) not null,
	type varchar(50) not null,
	first_name varchar(50),
	last_name varchar(50),
	room_name varchar(50),
	floor int,
	max_persons int
);


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


