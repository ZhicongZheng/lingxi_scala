CREATE TABLE IF NOT EXISTS users(
 id             SERIAL PRIMARY KEY,
 username       VARCHAR (128)    UNIQUE NOT NULL,
 password       VARCHAR (256)    NOT NULL,
 avatar         VARCHAR         NOT NULL DEFAULT '',
 phone          VARCHAR (50)    NOT NULL DEFAULT '',
 email          VARCHAR (355)   NOT NULL DEFAULT '',
 nick_name      VARCHAR (355)   NOT NULL DEFAULT '',
 create_by      BIGINT          NOT NULL DEFAULT 0,
 update_by      BIGINT          NOT NULL DEFAULT 0,
 create_at      TIMESTAMP       NOT NULL DEFAULT current_timestamp,
 update_at      TIMESTAMP       NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS user_roles(
 id             SERIAL PRIMARY KEY,
 user_id        BIGINT    NOT NULL,
 role_id        BIGINT    NOT NULL
);

CREATE TABLE IF NOT EXISTS roles(
 id             SERIAL PRIMARY KEY,
 code           VARCHAR (50)    UNIQUE NOT NULL,
 name           VARCHAR (50)    NOT NULL,
 create_by      BIGINT          NOT NULL DEFAULT 0,
 update_by      BIGINT          NOT NULL DEFAULT 0,
 create_at      TIMESTAMP       NOT NULL DEFAULT current_timestamp,
 update_at      TIMESTAMP       NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS role_permissions(
 id             SERIAL PRIMARY KEY,
 role_id        BIGINT    NOT NULL,
 permission_id  BIGINT    NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions(
 id             SERIAL PRIMARY KEY,
 type           VARCHAR (50)    NOT NULL,
 value          VARCHAR (512)   NOT NULL,
 name           VARCHAR (50)    NOT NULL,
 create_by      BIGINT          NOT NULL DEFAULT 0,
 update_by      BIGINT          NOT NULL DEFAULT 0,
 create_at      TIMESTAMP       NOT NULL DEFAULT current_timestamp,
 update_at      TIMESTAMP       NOT NULL DEFAULT current_timestamp
);

INSERT INTO users(id, username, password, avatar, email, nick_name) VALUES (1, 'lingxi', '$2a$10$ITUbtucJissi2TETUz2bZuckT7uc5GZrKl9KZCWZEvDKmuypzerji', '/avatar.png', 'admin@qq.com', '灵犀');

INSERT INTO roles(id, code, name) VALUES (1, 'SUPER_ADMIN', '超级管理员');

INSERT INTO user_roles(user_id, role_id) VALUES (1, 1);

INSERT INTO role_permissions(role_id, permission_id) VALUES (1, 1)

INSERT INTO permissions(type, value, name) VALUES('api', 'interfaces.controller.UserController.listByPage', '获取用户列表')
