DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS usernames;


/*Credits for conceiving database plan goes to Nata nkhur17 */




CREATE TABLE chats(
    id INT NOT NULL,
    name VARCHAR NOT NULL,
    description VARCHAR DEFAULT NULL,
    visibility NUMBER(1) NOT NULL,
    limit INT DEFAULT 100,
    numMembers INT,
    creationDate DATE NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE users(
    id INT NOT NULL,
    chatID INT NOT NULL,
    username VARCHAR NOT NULL,
    PRIMARY KEY(id)
    FOREIGN KEY (chatID) REFERENCES chats(id)
);


CREATE TABLE messages(
    id INT NOT NULL AUTO_INCREMENT,
    chatID INT NOT NULL,
    userID INT NOT NULL,
    content VARCHAR NOT NULL,
    date DATE NOT NULL,
    PRIMARY KEY(id)
    FOREIGN KEY (chatID) REFERENCES chats(id)
    FOREIGN KEY (userID) REFERENCES users(id)
);

CREATE TABLE tags(
    id INT NOT NULL AUTO_INCREMENT,
    word VARCHAR NOT NULL,
    chatID INT NOT NULL,
    PRIMARY KEY(id)
    FOREIGN KEY (chatID) REFERENCES chats(id)
);

CREATE TABLE usernames(
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR NOT NULL,
    PRIMARY KEY(id)
);

