
CREATE TABLE IF NOT EXISTS "purchase_details" (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    purchase_date TIMESTAMP NOT NULl
);

CREATE TABLE IF NOT EXISTS "tickets" (
    id SERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,
    event_name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL
);

INSERT INTO tickets (quantity, event_name, price)
VALUES (100, 'some show', 700);
