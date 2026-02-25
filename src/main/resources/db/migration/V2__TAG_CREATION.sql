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
)