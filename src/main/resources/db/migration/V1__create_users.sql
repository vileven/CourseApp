CREATE TABLE IF NOT EXISTS users (
  id         BIGSERIAL PRIMARY KEY,
  role       INTEGER      NOT NULL CHECK (role >= 0 AND role <= 2), -- 0 - admin, 1 - user, 2 - prof
  email      VARCHAR(50)  NOT NULL UNIQUE,
  password   VARCHAR(100) NOT NULL,
  first_name VARCHAR(50)  NOT NULL,
  last_name  VARCHAR(50)  NOT NULL,
  avatar     BYTEA,
  about      TEXT
) ;
