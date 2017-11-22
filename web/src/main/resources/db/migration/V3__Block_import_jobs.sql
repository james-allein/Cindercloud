CREATE TABLE block_import_jobs (
  id         BIGINT(20)      AUTO_INCREMENT PRIMARY KEY,
  active     BOOL            DEFAULT FALSE,
  from_block BIGINT UNSIGNED DEFAULT 0,
  to_block   BIGINT UNSIGNED DEFAULT 0,
  start_time DATETIME       DEFAULT NULL,
  end_time   DATETIME       DEFAULT NULL
);