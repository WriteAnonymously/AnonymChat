drop schema if exists anonym_chat_schema;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS usernames;
DROP TABLE IF EXISTS members;


create schema anonym_chat_schema;
use anonym_chat_schema;

/*Credits for conceiving database plan goes to Nata nkhur17 */


CREATE TABLE chats(
    id number NOT NULL,
    name VARCHAR NOT NULL,
    description VARCHAR DEFAULT NULL,
    visibility NUMBER(1) NOT NULL,
    limit number DEFAULT 100,
    creationDate DATE NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE users(
    id number NOT NULL,
    chatID number NOT NULL,
    username VARCHAR NOT NULL,
    PRIMARY KEY(id)
    FOREIGN KEY (chatID) REFERENCES chats(id)
);


CREATE TABLE messages(
    id number NOT NULL AUTO_INCREMENT,
    chatID number NOT NULL,
    userID number NOT NULL,
    content VARCHAR NOT NULL,
    date DATE NOT NULL,
    PRIMARY KEY(id)
    FOREIGN KEY (chatID) REFERENCES chats(id)
    FOREIGN KEY (userID) REFERENCES users(id)
);

CREATE TABLE tags(
    id number NOT NULL AUTO_INCREMENT,
    word VARCHAR NOT NULL,
    chatID number NOT NULL,
    PRIMARY KEY(id)
    FOREIGN KEY (chatID) REFERENCES chats(id)
);

CREATE TABLE usernames(
    id number NOT NULL AUTO_INCREMENT,
    username VARCHAR NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE members(
    id number NOT NULL AUTO_INCREMENT,
    user_id number not null,
    name
);
