create table users(
  id serial primary key,
  birth_date date not null
);
create sequence seq_user_id start with 1 increment BY 1;