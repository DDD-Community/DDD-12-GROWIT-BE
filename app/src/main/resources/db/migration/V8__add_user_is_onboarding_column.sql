ALTER TABLE users
  ADD is_onboarding BOOLEAN;

UPDATE users
SET is_onboarding = 'false'
WHERE is_onboarding IS NULL;
ALTER TABLE users
  ALTER COLUMN is_onboarding SET NOT NULL;
