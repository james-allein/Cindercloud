CREATE TABLE blocks (
  hash             VARCHAR(66) PRIMARY KEY,
  height           BIGINT UNSIGNED NOT NULL,
  parent_hash      VARCHAR(66)     DEFAULT NULL,
  sha3_uncles      VARCHAR(66)     DEFAULT NULL,
  mined_by         VARCHAR(42)     DEFAULT NULL,
  difficulty       VARCHAR(100)    DEFAULT NULL,
  difficulty_total VARCHAR(100)    DEFAULT NULL,
  gas_limit        VARCHAR(100)    DEFAULT NULL,
  gas_used         VARCHAR(100)    DEFAULT NULL,
  receipts_root    VARCHAR(66)     DEFAULT NULL,
  size             BIGINT UNSIGNED DEFAULT NULL,
  timestamp        BIGINT UNSIGNED DEFAULT NULL,
  nonce            BIGINT UNSIGNED DEFAULT NULL,
  extra_data       TEXT            DEFAULT NULL,
  mix_hash         VARCHAR(66)     DEFAULT NULL
);