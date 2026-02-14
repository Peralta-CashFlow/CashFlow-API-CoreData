-- Category table creation
CREATE TABLE tb_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    color VARCHAR(20) NULL,
    icon VARCHAR(20),
    active BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL
);

-- Generating category base
INSERT INTO tb_category
(name, color, icon, active, user_id)
VALUES('Food', '#c43030', '🍗', 1, 5);
INSERT INTO tb_category
(name, color, icon, active, user_id)
VALUES('Miscelaneous', NULL, NULL, 1, 5);
INSERT INTO tb_category
(name, color, icon, active, user_id)
VALUES('Salary', '#12e80e', '💵', 1, 5);