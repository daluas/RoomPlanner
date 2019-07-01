delete from reservations;
delete from users_roles;
DELETE FROM users;
ALTER SEQUENCE seq_user_id RESTART WITH 1;
UPDATE users SET id=nextval('seq');
ALTER SEQUENCE seq_reservation_id RESTART WITH 1;
UPDATE reservations SET id=nextval('seq');
