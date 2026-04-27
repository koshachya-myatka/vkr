DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'metal_data_mart') THEN
        PERFORM dblink_exec('dbname=' || current_database(), 'CREATE DATABASE metal_data_mart');
    END IF;
END
$$;

\c metal_data_mart

CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    post_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    content TEXT,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO users (name, surname) 
    VALUES
    ('Анна', 'Парамонова'),
    ('Анастасия', 'Волкова'),
    ('Вася', 'Гадов');