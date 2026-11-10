ALTER TABLE trial
    DROP COLUMN created_by,
    DROP COLUMN created_date;

ALTER TABLE trial
    ALTER COLUMN trial_name DROP DEFAULT,
    ALTER COLUMN trial_name DROP NOT NULL,
    ALTER COLUMN trial_name TYPE text COLLATE "default";
