DROP TABLE IF EXISTS GENRE;
CREATE TABLE GENRE(
  ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  NAME VARCHAR(255) NOT NULL,
  CODE VARCHAR(32) NOT NULL
);

DROP TABLE IF EXISTS AUTHOR;
CREATE TABLE AUTHOR(
  ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  FIRST_NAME VARCHAR(255) NOT NULL,
  FAMILY_NAME VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS BOOK;
CREATE TABLE BOOK(
  ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  GENRE_ID INT NOT NULL,
  TITLE VARCHAR(255)  NOT NULL,
  ISBN VARCHAR(255) NOT NULL UNIQUE,
  PUBLICATION_YEAR INT NOT NULL,
  NUMBER_OF_PAGES INT NOT NULL,
  PUBLISHER VARCHAR(255) NULL,
  FOREIGN KEY (GENRE_ID) REFERENCES GENRE (ID)
);

DROP TABLE IF EXISTS BOOK_AUTHOR;
CREATE TABLE BOOK_AUTHOR(
  BOOK_ID INT NOT NULL,
  AUTHOR_ID INT NOT NULL,
  FOREIGN KEY (BOOK_ID) REFERENCES BOOK (ID),
  FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR (ID)
);