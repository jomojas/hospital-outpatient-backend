-- Add manufacturer column to drug table
-- MySQL
ALTER TABLE drug
    ADD COLUMN manufacturer VARCHAR(100) NULL AFTER retail_price;

-- Optional: if you prefer NOT NULL, run after you backfill existing rows
-- ALTER TABLE drug MODIFY COLUMN manufacturer VARCHAR(100) NOT NULL;

