-- Category table creation
CREATE TABLE IF NOT EXISTS tb_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    color VARCHAR(20) NULL,
    icon VARCHAR(20) NULL,
    active BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT NULL,
    updated_at TIMESTAMP NULL
);

-- Generating category base
INSERT INTO tb_category
(name, color, icon, active, user_id, created_by)
VALUES('Food', '#c43030', '🍗', 1, 5, 5);
INSERT INTO tb_category
(name, color, icon, active, user_id, created_by, updated_by, updated_at)
VALUES('Miscelaneous', NULL, NULL, 1, 5, 5, 5, CURRENT_TIMESTAMP);
INSERT INTO tb_category
(name, color, icon, active, user_id, created_by)
VALUES('Salary', '#12e80e', '💵', 1, 5, 5);

-- Tag table creation
CREATE TABLE IF NOT EXISTS tb_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    category_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT NULL,
    updated_at TIMESTAMP NULL,
    CONSTRAINT fk_tag_category FOREIGN KEY (category_id) REFERENCES tb_category(id)
);

-- Generating tag base
INSERT INTO tb_tag
(name, category_id, created_by)
VALUES('Groceries', 1, 5);