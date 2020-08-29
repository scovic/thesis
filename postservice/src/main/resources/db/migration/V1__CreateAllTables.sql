CREATE TABLE IF NOT EXISTS "posts" (
    id SERIAL PRIMARY KEY,
    text VARCHAR(512) NOT NULL,
    author_id INT NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS "comments" (
    id SERIAL PRIMARY KEY,
    text VARCHAR(256) NOT NULL,
    author_id INT NOT NULL,
    post_id INT NOT NULL REFERENCES posts (id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

