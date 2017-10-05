create table transactions(
  hash varchar(66) primary key,
  block_hash varchar(66) default null,
  gas_price bigint default null,
  gas bigint default null,
  transaction_index bigint default null,
  input text default null,
  nonce bigint default null,
  from_address varchar(42) not null,
  to_address varchar(42) not null,
  s text default null,
  r text default null,
  v int default null
);