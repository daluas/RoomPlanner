insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('westeros@yahoo.com', '{bcrypt}$2a$04$A6RBLOQHiGeQILAXss/L2.dkyfRDXeoEspNGiQDlzYJSUfFB0tWWe','ROOM', null, null, 'Westeros', 8, 20);

insert into users_roles (user_id, role_id) values (3,2);

insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('neverland@yahoo.com', '{bcrypt}$2a$04$CapyfM0W9cilqtcPZjpBS.O4DU2E8nD7Om6UnY7dIljCYIb7U5sAW','ROOM', null, null, 'Neverland', 4, 5);

insert into users_roles (user_id, role_id) values (4,2);