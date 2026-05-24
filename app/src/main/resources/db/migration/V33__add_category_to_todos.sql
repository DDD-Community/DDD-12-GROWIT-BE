ALTER TABLE todos
    ADD category VARCHAR(32);

UPDATE todos
SET category = 'URGENT'
WHERE category IS NULL;

ALTER TABLE todos
    ALTER COLUMN category SET NOT NULL;
