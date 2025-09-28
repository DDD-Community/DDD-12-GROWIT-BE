-- Add is_used column to promotions table
ALTER TABLE promotions 
ADD COLUMN is_used BOOLEAN NOT NULL DEFAULT FALSE;

-- Update existing promotions to set is_used = false (default value)
UPDATE promotions SET is_used = FALSE WHERE is_used IS NULL;