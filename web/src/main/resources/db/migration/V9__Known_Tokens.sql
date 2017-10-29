CREATE TABLE tokens (
  id                 BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  coinmarketcap_name VARCHAR(100)           DEFAULT NULL,
  address            VARCHAR(42)  NOT NULL,
  name               VARCHAR(100) NOT NULL,
  slug               VARCHAR(100) NOT NULL,
  image              TEXT                   DEFAULT NULL,
  decimals           TINYINT                DEFAULT 18
)