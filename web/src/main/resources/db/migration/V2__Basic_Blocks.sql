create table blocks(
  hash varchar(66) primary key,
  height BIGINT not null,
  parent_hash varchar(66) default null,
  sha3_uncles varchar(66) default null,
  mined_by varchar(42) default null,
  difficulty bigint default null,
  difficulty_total bigint default null,
  gas_limit bigint default null,
  gas_used bigint default null,
  receipts_root varchar(66) default null,
  size bigint default null,
  timestamp bigint default null,
  nonce bigint default null,
  extra_data text default null,
  mix_hash varchar(66) default null
);