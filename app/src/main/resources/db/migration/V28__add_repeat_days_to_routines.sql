-- Add repeat_days column to routines table to store comma-separated list of day of week values
ALTER TABLE routines ADD COLUMN repeat_days VARCHAR(50);

-- Add comment to describe the column format
COMMENT ON COLUMN routines.repeat_days IS 'Comma-separated list of day of week values (e.g., MONDAY,WEDNESDAY,FRIDAY)';