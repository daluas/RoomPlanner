insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('narnia@yahoo.com', '{bcrypt}$2a$04$.XUsqgp0fzBqNguNr7Pi/O4kvLKO7nQWUOv.ugmOHOLX12usCSXIm','ROOM', null, null, 'Narnia', 4, 8);

insert into users_roles (user_id, role_id) values (5,2);

insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('morrowind@yahoo.com', '{bcrypt}$2a$04$fSme6rBNJnTw7KzloKRj4.RKwxnskcxiKelBuvlFFLpdzJazKcbo.','ROOM', null, null, 'Morrowind', 8, 10);

insert into users_roles (user_id, role_id) values (6,2);

insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('atlantis@yahoo.com', '{bcrypt}$2a$04$8ISsDsPblN2vE85Lio4y8uirwiZi7ipMPF31j5yaXzHQ43l0iVv56','ROOM', null, null, 'Atlantis', 5, 26);

insert into users_roles (user_id, role_id) values (7,2);

insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('asgard@yahoo.com', '{bcrypt}$2a$04$IkqUr.aqziiLMPmJOv80n.dkRgBdOgI32GEoE3Y9oMstyUJbY1deO','ROOM', null, null, 'Asgard', 4, 7);

insert into users_roles (user_id, role_id) values (8,2);

insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-21 14:30:20','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-21 15:30:20','YYYY-MM-DD HH24:MI:SS'), 'Demo meeting', 1, 2);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-22 12:30:20','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-22 13:30:20','YYYY-MM-DD HH24:MI:SS'), null, 1, 2);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-27 00:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-27 23:59:00','YYYY-MM-DD HH24:MI:SS'), 'All day reservation', 1, 3);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-27 12:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-27 15:00:00','YYYY-MM-DD HH24:MI:SS'), 'Retro meeting', 1, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-26 12:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-26 17:00:00','YYYY-MM-DD HH24:MI:SS'), null, 1, 7);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-26 09:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-26 16:00:00','YYYY-MM-DD HH24:MI:SS'), 'Client meeting', 1, 2);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-26 00:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-26 23:59:59','YYYY-MM-DD HH24:MI:SS'), 'Occupying this all day', 1, 6);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-28 10:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-28 11:30:00','YYYY-MM-DD HH24:MI:SS'), 'Daily', 1, 8);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-28 11:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-28 13:30:00','YYYY-MM-DD HH24:MI:SS'), 'Training Agile', 1, 8);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-06-28 14:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-28 16:30:00','YYYY-MM-DD HH24:MI:SS'), 'Demo', 1, 8);

