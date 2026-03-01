ALTER TABLE user_advice_status
    ADD COLUMN IF NOT EXISTS is_goal_onboarding_completed BOOLEAN NOT NULL DEFAULT FALSE;
