CREATE TABLE IF NOT EXISTS users
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(255) UNIQUE NOT NULL,
    password  VARCHAR(1024)       NOT NULL,
    avatar    VARCHAR(1024)       NOT NULL DEFAULT '',
    phone     VARCHAR(50)         NOT NULL DEFAULT '',
    email     VARCHAR(255)        NOT NULL DEFAULT '',
    nick_name VARCHAR(255)        NOT NULL DEFAULT '',
    create_by BIGINT              NOT NULL DEFAULT 0,
    update_by BIGINT              NOT NULL DEFAULT 0,
    create_at TIMESTAMP           NOT NULL DEFAULT current_timestamp,
    update_at TIMESTAMP           NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS user_roles
(
    id      SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    id        SERIAL PRIMARY KEY,
    code      VARCHAR(255) UNIQUE NOT NULL,
    name      VARCHAR(255)        NOT NULL,
    create_by BIGINT              NOT NULL DEFAULT 0,
    update_by BIGINT              NOT NULL DEFAULT 0,
    create_at TIMESTAMP           NOT NULL DEFAULT current_timestamp,
    update_at TIMESTAMP           NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS role_permissions
(
    id            SERIAL PRIMARY KEY,
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions
(
    id        SERIAL PRIMARY KEY,
    type      VARCHAR(255) NOT NULL,
    value     VARCHAR(512) NOT NULL,
    name      VARCHAR(255) NOT NULL,
    create_by BIGINT       NOT NULL DEFAULT 0,
    update_by BIGINT       NOT NULL DEFAULT 0,
    create_at TIMESTAMP    NOT NULL DEFAULT current_timestamp,
    update_at TIMESTAMP    NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS tags
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    create_at TIMESTAMP    NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS categories
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    parent    BIGINT       NOT NULL DEFAULT -1,
    create_at TIMESTAMP    NOT NULL DEFAULT current_timestamp
);


CREATE TABLE IF NOT EXISTS actions
(
    id             SERIAL PRIMARY KEY,
    typ            INT       NOT NULL,
    resource_id      BIGINT    NOT NULL,
    remote_address inet      NOT NULL,
    create_by      BIGINT    NOT NULL DEFAULT 0,
    update_by      BIGINT    NOT NULL DEFAULT 0,
    create_at      TIMESTAMP NOT NULL DEFAULT current_timestamp,
    update_at      TIMESTAMP NOT NULL DEFAULT current_timestamp
);


CREATE TABLE IF NOT EXISTS articles
(
    id           SERIAL4 PRIMARY KEY,
    title        VARCHAR   NOT NULL,
    introduction VARCHAR   NOT NULL DEFAULT '',
    front_cover  VARCHAR,
    content_md   TEXT   NOT NULL DEFAULT '',
    content_html TEXT   NOT NULL DEFAULT '',
    status       SMALLINT  not null DEFAULT 0,
    tags         BIGINT[]  NOT NULL DEFAULT ARRAY[]::BIGINT[],
    category     BIGINT,
    create_by    BIGINT    NOT NULL DEFAULT 0,
    update_by    BIGINT    NOT NULL DEFAULT 0,
    create_at    TIMESTAMP NOT NULL DEFAULT current_timestamp,
    update_at    TIMESTAMP NOT NULL DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS comments
(
    id             SERIAL PRIMARY KEY,
    content        VARCHAR   NOT NULL,
    user_name      VARCHAR   NOT NULL DEFAULT '',
    user_email     VARCHAR,
    reply_to       BIGINT    NOT NULL DEFAULT -1,
    resource_id    BIGINT    NOT NULL,
    remote_address inet      NOT NULL,
    allow_notify   BOOLEAN   NOT NULL DEFAULT FALSE,
    create_by      BIGINT    NOT NULL DEFAULT 0,
    update_by      BIGINT    NOT NULL DEFAULT 0,
    create_at      TIMESTAMP NOT NULL DEFAULT current_timestamp,
    update_at      TIMESTAMP NOT NULL DEFAULT current_timestamp
);



INSERT INTO users(id, username, password, avatar, email, nick_name)
VALUES (1, 'lingxi', '$2a$10$ITUbtucJissi2TETUz2bZuckT7uc5GZrKl9KZCWZEvDKmuypzerji',
        'https://oss-lingxi.oss-cn-beijing.aliyuncs.com/jpg/%E5%A4%B4%E5%83%8F31.jpg', 'admin@qq.com',
        '灵犀');

INSERT INTO roles(id, code, name)
VALUES (1, 'SUPER_ADMIN', '超级管理员');

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 1);

INSERT INTO role_permissions(role_id, permission_id)
VALUES (1, 1);

INSERT INTO permissions(type, value, name)
VALUES ('api', 'interfaces.controller.UserController.listByPage', '获取用户列表')
