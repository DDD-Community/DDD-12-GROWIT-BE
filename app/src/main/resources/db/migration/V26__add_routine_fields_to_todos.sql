-- Remove routine fields from todos table to match current ToDoEntity structure
ALTER TABLE todos 
DROP COLUMN IF EXISTS routine_start_date,
DROP COLUMN IF EXISTS routine_end_date,
DROP COLUMN IF EXISTS routine_repeat_type;

-- Add routine_id column to match current ToDoEntity
ALTER TABLE todos 
ADD COLUMN routine_id VARCHAR(255);

-- Keep deleted_at column for soft delete functionality (already exists in server)