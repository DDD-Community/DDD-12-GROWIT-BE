-- Create mentor_advice table
CREATE TABLE mentor_advice (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    goal_id VARCHAR(36) NOT NULL,
    is_checked BOOLEAN NOT NULL DEFAULT FALSE,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    
    UNIQUE(user_id, goal_id)
);

CREATE INDEX idx_mentor_advice_user_id ON mentor_advice (user_id);
CREATE INDEX idx_mentor_advice_goal_id ON mentor_advice (goal_id);
CREATE INDEX idx_mentor_advice_user_goal ON mentor_advice (user_id, goal_id);

-- Create mentor_advice_kpts table
CREATE TABLE mentor_advice_kpts (
    id BIGSERIAL PRIMARY KEY,
    mentor_advice_id BIGINT NOT NULL,
    keep VARCHAR(500) NOT NULL,
    problem VARCHAR(500) NOT NULL,
    try_next VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    
    CONSTRAINT fk_mentor_advice_kpts_mentor_advice_id 
        FOREIGN KEY (mentor_advice_id) REFERENCES mentor_advice (id),
    
    CONSTRAINT uc_mentor_advice_kpts_mentor_advice_id 
        UNIQUE (mentor_advice_id)
);