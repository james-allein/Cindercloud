create table wallet_addresses (
  id bigserial primary key,
  owner       varchar(100),
  address     varchar(200),
  secret_key  varchar(100),
  wallet_type varchar(25)
);