CREATE TABLE IF NOT EXISTS "event_object" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    type VARCHAR(20) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS "performer" (
    id SERIAL PRIMARY KEY,
    stage_id INT NOT NULL REFERENCES event_object (id) ON DELETE CASCADE,
    name VARCHAR(512) NOT NULL,
    start_time TIMESTAMP DEFAULT now()
);