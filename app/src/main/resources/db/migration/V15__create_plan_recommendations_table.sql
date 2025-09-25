CREATE TABLE plan_recommendations (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    goal_id VARCHAR(36) NOT NULL,
    plan_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,

    UNIQUE(user_id, goal_id, plan_id)
);

CREATE INDEX idx_plan_recommendations_user_id ON plan_recommendations (user_id);
CREATE INDEX idx_plan_recommendations_goal_id ON plan_recommendations (goal_id);
CREATE INDEX idx_plan_recommendations_plan_id ON plan_recommendations (plan_id);
