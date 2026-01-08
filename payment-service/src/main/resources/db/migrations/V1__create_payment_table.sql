USE booking_platform;

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    status VARCHAR(255) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    payment_date DATETIME NOT NULL
);
