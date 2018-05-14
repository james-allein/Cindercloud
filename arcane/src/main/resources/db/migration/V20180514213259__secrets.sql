create table secrets (
  id varchar(200) primary key,
  bigint user_id not null references users(id)
);
