drop schema if exists anonym_chat_schema;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS chats;


create schema anonym_chat_schema;
use anonym_chat_schema;

/*Credits for conceiving database plan goes to Nata nkhur17 */


CREATE TABLE chats(
    id bigint primary key auto_increment,
    name VARCHAR(100),
    description VARCHAR(1000) DEFAULT NULL,
    visibility varchar(20) NOT NULL,
    max_users_number int,
    creationDate DATE
);

CREATE TABLE users(
    id bigint primary key auto_increment,
    chatID bigint,
    username VARCHAR(100),
    FOREIGN KEY (chatID) REFERENCES chats(id)
);


CREATE TABLE messages(
    id bigint primary key AUTO_INCREMENT,
    chatID bigint,
    userID bigint,
    content longtext,
    date DATE,
    FOREIGN KEY (chatID) REFERENCES chats(id),
    FOREIGN KEY (userID) REFERENCES users(id)
);
