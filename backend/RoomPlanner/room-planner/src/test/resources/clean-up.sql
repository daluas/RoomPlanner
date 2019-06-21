delete from users_roles;
DELETE FROM users;
ALTER SEQUENCE seq_user_id RESTART WITH 1;
UPDATE users SET id=nextval('seq');

insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('sghitun@yahoo.com', '{bcrypt}$2a$10$vFMyZ6cn75jjhsWnxAZTK.8EG4LHQc264yXMlKmq8n4LvNF59xpg.','PERSON', 'Stefania', 'Ghitun', null, null, null);
insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('wonderland@yahoo.com', '{bcrypt}$2a$04$/1zqAcZawTi7nG.XRYLxo.dAEFZZxQ4SRswYFHTqDwqxw8WEj.gk.','ROOM', null, null, 'Wonderland', 5, 14);
insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('westeros@yahoo.com', '{bcrypt}$2a$04$A6RBLOQHiGeQILAXss/L2.dkyfRDXeoEspNGiQDlzYJSUfFB0tWWe','ROOM', null, null, 'Westeros', 8, 20);
insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('neverland@yahoo.com', '{bcrypt}$2a$04$CapyfM0W9cilqtcPZjpBS.O4DU2E8nD7Om6UnY7dIljCYIb7U5sAW','ROOM', null, null, 'Neverland', 4, 5);