ALTER TABLE germplasm
    DROP COLUMN created_by,
    DROP COLUMN created_date,
    DROP COLUMN breeding_method_sort,
    DROP COLUMN female_parent_gid,
    DROP COLUMN male_parent_gid;

ALTER TABLE germplasm
    ALTER COLUMN seed_source DROP DEFAULT,
    ALTER COLUMN seed_source DROP NOT NULL,
    ALTER COLUMN seed_source TYPE text COLLATE "default",
    ALTER COLUMN default_display_name DROP DEFAULT,
    ALTER COLUMN default_display_name DROP NOT NULL,
    ALTER COLUMN default_display_name TYPE text COLLATE "default",
    ALTER COLUMN accession_number DROP DEFAULT,
    ALTER COLUMN accession_number DROP NOT NULL,
    ALTER COLUMN accession_number TYPE text COLLATE "default";

