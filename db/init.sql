CREATE TABLE IF NOT EXISTS users(
 id             SERIAL PRIMARY KEY,
 username       VARCHAR (50)    UNIQUE NOT NULL,
 password       VARCHAR (50)    NOT NULL,
 avatar         VARCHAR         NOT NULL DEFAULT '',
 email          VARCHAR (355)   NOT NULL DEFAULT '',
 nick_name      VARCHAR (355)   NOT NULL DEFAULT '',
 create_by      BIGINT          NOT NULL DEFAULT 0,
 update_by      BIGINT          NOT NULL DEFAULT 0,
 create_at      TIMESTAMP       NOT NULL DEFAULT current_timestamp,
 update_at      TIMESTAMP       NOT NULL default current_timestamp
);