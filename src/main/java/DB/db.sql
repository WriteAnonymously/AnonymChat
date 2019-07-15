drop schema if exists anonym_chat_schema;
create schema anonym_chat_schema;
use anonym_chat_schema;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS tags;

CREATE TABLE chats(
    id bigint primary key auto_increment,
    name VARCHAR(100),
    description VARCHAR(1000) DEFAULT NULL,
    status varchar(20) NOT NULL,
    max_users_number int,
    creation_date DATETIME(4)
);

CREATE TABLE users(
    id bigint primary key auto_increment,
    chatid bigint,
    username VARCHAR(100),
    creation_date datetime(4),
    FOREIGN KEY (chatID) REFERENCES chats(id)
);

CREATE TABLE messages(
    id bigint primary key AUTO_INCREMENT,
    chatid bigint,
    userid bigint,
    content longtext,
    creation_date DATETIME(4),
    FOREIGN KEY (chatID) REFERENCES chats(id),
    FOREIGN KEY (userID) REFERENCES users(id)
);

CREATE TABLE usernames(
    id bigint primary key auto_increment,
    username VARCHAR(100)
);

CREATE TABLE tags(
    id bigint primary key auto_increment,
    name VARCHAR(100),
    chatid bigint
);

CREATE TABLE used_random_identificators(
    id varchar(100) primary key,
    chatid bigint,
    userid bigint,
    FOREIGN KEY (chatID) REFERENCES chats(id),
    FOREIGN KEY (userID) REFERENCES users(id)
);

CREATE TABLE not_used_random_identificators(
    id varchar(100) primary key,
    chatid bigint,
    FOREIGN KEY (chatID) REFERENCES chats(id)
);

CREATE TABLE guesses (
    id bigint primary key auto_increment,
    chatid bigint,
    word varchar(100),
    FOREIGN KEY (chatID) REFERENCES chats(id)
);
