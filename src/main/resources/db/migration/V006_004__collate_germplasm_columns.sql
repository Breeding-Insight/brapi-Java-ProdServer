CREATE COLLATION IF NOT EXISTS natural_sort (
    provider = icu,
    locale = 'en-u-kn-true'
    );

ALTER TABLE germplasm ALTER COLUMN accession_number SET DEFAULT '';
UPDATE germplasm SET accession_number = '' WHERE accession_number IS NULL;
ALTER TABLE germplasm ALTER COLUMN accession_number SET NOT NULL;
ALTER TABLE germplasm ALTER COLUMN accession_number TYPE text COLLATE natural_sort;

ALTER TABLE germplasm ALTER COLUMN default_display_name SET DEFAULT '';
UPDATE germplasm SET default_display_name = '' WHERE default_display_name IS NULL;
ALTER TABLE germplasm ALTER COLUMN default_display_name SET NOT NULL;
ALTER TABLE germplasm ALTER COLUMN default_display_name TYPE text COLLATE natural_sort;

ALTER TABLE germplasm ALTER COLUMN seed_source SET DEFAULT '';
UPDATE germplasm SET seed_source = '' WHERE seed_source IS NULL;
ALTER TABLE germplasm ALTER COLUMN seed_source SET NOT NULL;
ALTER TABLE germplasm ALTER COLUMN seed_source TYPE text COLLATE natural_sort;

ALTER TABLE germplasm
    ADD COLUMN
        male_parent_gid text NOT NULL COLLATE natural_sort
        GENERATED ALWAYS AS (COALESCE(additional_info #>> '{maleParentGid}', '')) STORED ;

ALTER TABLE germplasm
    ADD COLUMN
        female_parent_gid text NOT NULL COLLATE natural_sort
            GENERATED ALWAYS AS (COALESCE(additional_info #>> '{femaleParentGid}', '')) STORED ;

ALTER TABLE germplasm
    ADD COLUMN
        breeding_method_sort text NOT NULL COLLATE natural_sort
            GENERATED ALWAYS AS (COALESCE(additional_info #>> '{breedingMethod}', '')) STORED ;

ALTER TABLE germplasm
    ADD COLUMN created_date TIMESTAMP NOT NULL
        GENERATED ALWAYS AS (
            CASE
                WHEN additional_info #>> '{createdDate}' IS NULL
                    THEN TIMESTAMP '1970-01-01 00:00:00'
                ELSE
                    make_timestamp(
                            substring(additional_info #>> '{createdDate}' from 7 for 4)::int,  -- year
                            substring(additional_info #>> '{createdDate}' from 4 for 2)::int,  -- month
                            substring(additional_info #>> '{createdDate}' from 1 for 2)::int,  -- day
                            substring(additional_info #>> '{createdDate}' from 12 for 2)::int, -- hour
                            substring(additional_info #>> '{createdDate}' from 15 for 2)::int, -- minute
                            substring(additional_info #>> '{createdDate}' from 18 for 2)::int  -- second
                    )
                END
        ) STORED;

ALTER TABLE germplasm
    ADD COLUMN
        created_by text NOT NULL COLLATE natural_sort
            GENERATED ALWAYS AS (COALESCE(additional_info #>> '{createdBy,userName}', '')) STORED ;
