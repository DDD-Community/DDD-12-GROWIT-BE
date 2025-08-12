ALTER TABLE goals
    ADD category VARCHAR(255);

UPDATE goals
SET category = 'UNCATEGORIZED'
WHERE category IS NULL;
ALTER TABLE goals
    ALTER COLUMN category SET NOT NULL;
