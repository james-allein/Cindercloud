create table users (
  id bigserial primary key,
  external_id text not null,
  created_at  timestamp default now(),
  created_by  text      default null
);