UPDATE observation_unit_level_name SET level_order = 0 where level_order IS NULL;

ALTER TABLE observation_unit_level_name
ALTER COLUMN level_order SET NOT NULL;

DROP INDEX lvl_name_program_id_idx;

CREATE UNIQUE INDEX lvl_name_program_order_idx ON observation_unit_level_name (level_name, program_id, level_order);