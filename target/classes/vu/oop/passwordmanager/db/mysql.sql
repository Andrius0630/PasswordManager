CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
    user_name TEXT NOT NULL, 
    user_password TEXT NOT NULL
    );

CREATE TABLE IF NOT EXISTS users_passwords (
    user_id INTEGER, -- parent-key
    password_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    domain_name TEXT NOT NULL,
    domain_username TEXT NOT NULL,
    domain_password TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    );

INSERT INTO users(user_name, user_password)
VALUES ("admin", "admin");

INSERT INTO users_passwords(user_id, domain_name, domain_username, domain_password)
VALUES (1, "https://google.com", "admin", "admin");

SELECT * FROM users;
select * FROM users_passwords;