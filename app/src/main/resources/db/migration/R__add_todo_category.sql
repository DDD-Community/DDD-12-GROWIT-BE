ALTER TABLE todos ADD COLUMN IF NOT EXISTS category VARCHAR(16);

-- A previous production migration stored the four categories with different names.
-- Normalize those rows before Hibernate tries to map them to ToDoCategory.
UPDATE todos
SET category = CASE category
    WHEN 'URGENT' THEN 'NOW'
    WHEN 'CONSISTENT' THEN 'STEADY'
    WHEN 'DEFERABLE' THEN 'SKIP'
    WHEN 'DELETABLE' THEN 'DELETE'
    ELSE category
END
WHERE category IN ('URGENT', 'CONSISTENT', 'DEFERABLE', 'DELETABLE');

-- Preserve the old importance meaning for existing data while giving every todo a category.
UPDATE todos
SET category = CASE WHEN is_important THEN 'NOW' ELSE 'STEADY' END
WHERE category IS NULL;

ALTER TABLE todos ALTER COLUMN category SET NOT NULL;
