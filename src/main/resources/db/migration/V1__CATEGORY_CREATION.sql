-- Category table creation
CREATE TABLE IF NOT EXISTS tb_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    color VARCHAR(20) NULL,
    icon VARCHAR(20) CHARACTER SET utf8mb4,
    active BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    type VARCHAR(1) NOT NULL
);