-- Create saju_infos table
CREATE TABLE saju_infos
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    gender      VARCHAR(10)  NOT NULL,
    birth       DATE         NOT NULL,
    birth_hour  VARCHAR(10)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    deleted_at  TIMESTAMP    NULL,
    CONSTRAINT unique_saju_infos_user_id UNIQUE (user_id)
);

-- Add index on user_id for faster lookups
CREATE INDEX idx_saju_infos_user_id ON saju_infos (user_id);

-- Add index on deleted_at for soft delete queries
CREATE INDEX idx_saju_infos_deleted_at ON saju_infos (deleted_at);
