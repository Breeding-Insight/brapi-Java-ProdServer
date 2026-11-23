ALTER TABLE trial ALTER COLUMN trial_name SET DEFAULT '';
UPDATE trial SET trial_name = '' WHERE trial_name IS NULL;
ALTER TABLE trial ALTER COLUMN trial_name SET NOT NULL;
ALTER TABLE trial ALTER COLUMN trial_name TYPE text COLLATE natural_sort;

ALTER TABLE trial
    ADD COLUMN created_date TIMESTAMP NOT NULL
        GENERATED ALWAYS AS (
            CASE
                WHEN additional_info #>> '{createdDate}' IS NULL
                    THEN TIMESTAMP '1970-01-01 00:00:00'
                ELSE
                    make_timestamp(
                            substring(additional_info #>> '{createdDate}' from 1 for 4)::int,  -- year
                            substring(additional_info #>> '{createdDate}' from 6 for 2)::int,  -- month
                            substring(additional_info #>> '{createdDate}' from 9 for 2)::int,  -- day
                            0, 0, 0
                    )
                END
        ) STORED;

ALTER TABLE trial
    ADD COLUMN
        created_by text NOT NULL COLLATE natural_sort
            GENERATED ALWAYS AS (COALESCE(additional_info #>> '{createdBy,userName}', '')) STORED ;
