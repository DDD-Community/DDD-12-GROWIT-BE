ALTER TABLE todos ADD COLUMN IF NOT EXISTS category VARCHAR(16);

-- Preserve the old importance meaning for existing data while giving every todo a category.
UPDATE todos
SET category = CASE WHEN is_important THEN 'NOW' ELSE 'STEADY' END
WHERE category IS NULL;

ALTER TABLE todos ALTER COLUMN category SET NOT NULL;
