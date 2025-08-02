CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       lastname VARCHAR(100) NOT NULL
);


INSERT INTO users (name, lastname) VALUES ('Alice', 'Johnson');
INSERT INTO users (name, lastname) VALUES ('Bob', 'Smith');