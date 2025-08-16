ALTER TABLE goals
  ADD update_status VARCHAR(255);

UPDATE goals
SET update_status = 'UPDATABLE'
WHERE update_status IS NULL;
ALTER TABLE goals
  ALTER COLUMN update_status SET NOT NULL;
