ALTER TABLE todos ADD COLUMN category VARCHAR(10) NOT NULL DEFAULT 'NOW';

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'todos'
          AND column_name = 'is_important'
    ) THEN
        EXECUTE 'UPDATE todos SET category = CASE WHEN is_important = true THEN ''NOW'' ELSE ''DELETE'' END';
        ALTER TABLE todos DROP COLUMN is_important;
    END IF;
END $$;
