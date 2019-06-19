INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri,
 scope, access_token_validity, refresh_token_validity, resource_ids,
 authorized_grant_types, additional_information) VALUES ('browser',
 '{bcrypt}$2a$10$gPhlXZfms0EpNHX0.HHptOhoFD1AoxSr/yUIdTqA8vtjeP4zi0DDu',
  null, 'READ,WRITE', '3600', '10000',
  'roomplanner', 'authorization_code,password,refresh_token,implicit', '{}');
insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('sghitun@yahoo.com', '{bcrypt}$2a$10$vFMyZ6cn75jjhsWnxAZTK.8EG4LHQc264yXMlKmq8n4LvNF59xpg.','PERSON', 'Stefania', 'Ghitun', null, null, null);
insert into users (email, password, type, first_name, last_name, room_name, floor, max_persons)
values('wonderland@yahoo.com', '{bcrypt}$2a$04$/1zqAcZawTi7nG.XRYLxo.dAEFZZxQ4SRswYFHTqDwqxw8WEj.gk.','ROOM', null, null, 'Wonderland', 5, 14);
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