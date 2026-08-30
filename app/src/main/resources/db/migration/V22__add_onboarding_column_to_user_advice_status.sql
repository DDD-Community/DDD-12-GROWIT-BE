ALTER TABLE user_advice_status
    ADD COLUMN is_goal_onboarding_completed BOOLEAN NOT NULL DEFAULT FALSE;
