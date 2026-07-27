create schema if not exists anonym_chat_schema
    character set utf8mb4 collate utf8mb4_unicode_ci;
use anonym_chat_schema;

CREATE TABLE IF NOT EXISTS chats(
    id bigint primary key auto_increment,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000) DEFAULT NULL,
    status varchar(20) NOT NULL,
    max_users_number int NOT NULL DEFAULT 100,
    creation_date DATETIME(4) NOT NULL,
    INDEX idx_chats_status_created (status, creation_date)
);

CREATE TABLE IF NOT EXISTS users(
    id bigint primary key auto_increment,
    chatid bigint NOT NULL,
    username VARCHAR(100) NOT NULL,
    creation_date datetime(4) NOT NULL,
    FOREIGN KEY (chatID) REFERENCES chats(id) ON DELETE CASCADE,
    INDEX idx_users_chat (chatid),
    UNIQUE KEY uq_users_chat_username (chatid, username)
);

CREATE TABLE IF NOT EXISTS messages(
    id bigint primary key AUTO_INCREMENT,
    chatid bigint NOT NULL,
    userid bigint NOT NULL,
    content text NOT NULL,
    creation_date DATETIME(4) NOT NULL,
    FOREIGN KEY (chatID) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_messages_chat_created (chatid, creation_date)
);

CREATE TABLE IF NOT EXISTS usernames(
    id bigint primary key auto_increment,
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tags(
    id bigint primary key auto_increment,
    name VARCHAR(100) NOT NULL,
    chatid bigint NOT NULL,
    FOREIGN KEY (chatID) REFERENCES chats(id) ON DELETE CASCADE,
    INDEX idx_tags_name (name),
    INDEX idx_tags_chat (chatid)
);

CREATE TABLE IF NOT EXISTS used_random_identificators(
    id varchar(100) primary key,
    chatid bigint NOT NULL,
    userid bigint NOT NULL,
    FOREIGN KEY (chatID) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS not_used_random_identificators(
    id varchar(100) primary key,
    chatid bigint NOT NULL,
    FOREIGN KEY (chatID) REFERENCES chats(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS guesses (
    id bigint primary key auto_increment,
    chatid bigint NOT NULL UNIQUE,
    word varchar(100) NOT NULL,
    FOREIGN KEY (chatID) REFERENCES chats(id) ON DELETE CASCADE
);

INSERT IGNORE INTO usernames (username) VALUES
    ('Afterglow'), ('Blue Hour'), ('Cinder'), ('Daybreak'),
    ('Echo'), ('Fable'), ('Foxglove'), ('Halcyon'),
    ('Ink'), ('Juniper'), ('Lumen'), ('Morrow'),
    ('Nightjar'), ('Orbit'), ('Paper Moon'), ('Quill'),
    ('Rook'), ('Solace'), ('Tide'), ('Umbra'),
    ('Velvet'), ('Wander'), ('Yarrow'), ('Zephyr');
