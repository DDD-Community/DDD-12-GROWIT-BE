ALTER TABLE todos ADD COLUMN category VARCHAR(10) NOT NULL DEFAULT 'NOW';
UPDATE todos SET category = CASE WHEN is_important = true THEN 'NOW' ELSE 'DELETE' END;
ALTER TABLE todos DROP COLUMN is_important;
