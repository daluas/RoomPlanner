insert into users (email, password, type, first_name, last_name, room_name, floor_id, max_persons)
values('ageorge@yahoo.com', '{bcrypt}$2a$10$vFMyZ6cn75jjhsWnxAZTK.8EG4LHQc264yXMlKmq8n4LvNF59xpg.','PERSON', 'Adrian', 'George', null, null, null);
insert into users (email, password, type, first_name, last_name, room_name, floor_id, max_persons)
values('ipopescu@yahoo.com', '{bcrypt}$2a$10$vFMyZ6cn75jjhsWnxAZTK.8EG4LHQc264yXMlKmq8n4LvNF59xpg.','PERSON', 'Ion', 'Popescu', null, null, null);

insert into users_roles values (9,3);
insert into users_roles values (10,3);
insert into users_roles values (9,1);
insert into users_roles values (10,1);

insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 7:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 8:00:00','YYYY-MM-DD HH24:MI:SS'), 'Demo meeting', 1, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 8:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 9:00:00','YYYY-MM-DD HH24:MI:SS'), null, 8, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 9:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 12:00:00','YYYY-MM-DD HH24:MI:SS'), 'Reservation', 1, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 12:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 13:00:00','YYYY-MM-DD HH24:MI:SS'), 'Retro meeting', 1, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 14:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 14:30:00','YYYY-MM-DD HH24:MI:SS'), null, 9, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 16:00:00','YYYY-MM-DD HH24:MI:SS'), 'Client meeting', 9, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 16:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 17:30:00','YYYY-MM-DD HH24:MI:SS'), 'Occupying this', 1, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 18:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 19:30:00','YYYY-MM-DD HH24:MI:SS'), 'Daily', 8, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 19:30:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 20:00:00','YYYY-MM-DD HH24:MI:SS'), 'Training Agile', 9, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 6:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 7:00:00','YYYY-MM-DD HH24:MI:SS'), 'Demo', 8, 5);
insert into reservations (start_date, end_date, description, user_id, room_id) values (TO_TIMESTAMP('2019-07-18 07:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-18 07:30:00','YYYY-MM-DD HH24:MI:SS'), 'Daily', 9, 5);