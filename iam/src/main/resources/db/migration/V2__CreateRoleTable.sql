CREATE TABLE IF NOT EXISTS "roles" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

INSERT INTO "roles" (name)
VALUES ('USER');