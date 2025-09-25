CREATE TABLE user_status (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL UNIQUE,
    last_seen_date DATE,
    streak_len INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

CREATE INDEX idx_user_status_user_id ON user_status (user_id);
CREATE INDEX idx_user_status_last_seen_date ON user_status (last_seen_date);