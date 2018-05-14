create table secrets (
  id varchar(200) primary key,
  user_id bigint not null references users(id),
  created_at  timestamp default now(),
  created_by  text      default null
);
