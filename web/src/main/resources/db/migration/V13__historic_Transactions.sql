CREATE TABLE historic_transactions (
  hash              VARCHAR(66) PRIMARY KEY,
  block_hash        VARCHAR(66)     DEFAULT NULL,
  value             VARCHAR(100)    DEFAULT 0,
  gas_price         VARCHAR(100)    DEFAULT NULL,
  gas               VARCHAR(100)    DEFAULT NULL,
  transaction_index BIGINT UNSIGNED DEFAULT NULL,
  input             TEXT            DEFAULT NULL,
  nonce             BIGINT UNSIGNED DEFAULT NULL,
  from_address      VARCHAR(42)     DEFAULT NULL,
  to_address        VARCHAR(42)     DEFAULT NULL,
  creates           VARCHAR(42)     DEFAULT NULL,
  s                 TEXT            DEFAULT NULL,
  r                 TEXT            DEFAULT NULL,
  v                 INT             DEFAULT NULL
);

create index idx_h_tx_from_adddress
  on historic_transactions(from_address);

create index idx_h_tx_to_address
  on historic_transactions(to_address);

create index idx_h_tx_block_hash
  on historic_transactions(block_hash);

create index idx_h_tx_block_height
  on historic_transactions(block_height);

