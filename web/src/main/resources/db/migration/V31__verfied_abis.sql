create table verified_abis (
  id      SERIAL PRIMARY KEY,
  address VARCHAR(42),
  abi     text
);

create unique index idx_verified_abi_add
  on verified_abis (address);
