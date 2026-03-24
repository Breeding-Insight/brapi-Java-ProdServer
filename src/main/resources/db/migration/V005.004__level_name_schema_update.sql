DROP INDEX lvl_name_program_id_idx;

CREATE UNIQUE INDEX lvl_name_program_order_idx ON observation_unit_level_name (level_name, program_id, level_order);
