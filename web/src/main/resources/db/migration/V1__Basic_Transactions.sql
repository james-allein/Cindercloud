CREATE TABLE transactions (
  hash              VARCHAR(66) PRIMARY KEY,
  block_hash        VARCHAR(66)     DEFAULT NULL,
  value             varchar(100) DEFAULT 0,
  gas_price         varchar(100) DEFAULT NULL,
  gas               varchar(100) DEFAULT NULL,
  transaction_index BIGINT UNSIGNED DEFAULT NULL,
  input             TEXT            DEFAULT NULL,
  nonce             BIGINT UNSIGNED DEFAULT NULL,
  from_address      VARCHAR(42) NOT NULL,
  to_address        VARCHAR(42) NOT NULL,
  s                 TEXT            DEFAULT NULL,
  r                 TEXT            DEFAULT NULL,
  v                 INT             DEFAULT NULL
);