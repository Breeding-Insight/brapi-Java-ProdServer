ALTER TABLE observation_unit_level
    DROP COLUMN level_name,
    ADD COLUMN level_name INTEGER,
    ADD COLUMN level_order INTEGER;

ALTER TABLE observation_unit_position
    DROP COLUMN level_name,
    ADD COLUMN level_name INTEGER,
    ADD COLUMN level_order INTEGER;

ALTER TABLE study_observation_level
    DROP COLUMN level_name,
    ADD COLUMN level_name TEXT,
    ADD COLUMN level_order INTEGER;

DROP TABLE observation_unit_level_name;