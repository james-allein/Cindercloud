create table wallets (
  id bigserial primary key,
  secret_id   varchar(200) not null,
  user_id     bigint       not null references users (id),
  address     varchar(200) not null,
  wallet_type varchar(15)  not null,
  created_at timestamp default now(),
  created_by  text default null
);
