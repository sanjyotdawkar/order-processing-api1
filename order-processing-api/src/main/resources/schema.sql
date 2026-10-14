CREATE TABLE IF NOT EXISTS products (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description VARCHAR(1000),
    price       DECIMAL(10, 2) NOT NULL,
    stock       INT            NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  BIGINT         NOT NULL,
    quantity    INT            NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    created_at  DATETIME(6)    NOT NULL,
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products (id)
);
