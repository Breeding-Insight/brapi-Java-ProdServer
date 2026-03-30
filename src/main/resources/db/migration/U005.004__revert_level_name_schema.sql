ALTER TABLE observation_unit_level_name
ALTER COLUMN level_order DROP NOT NULL;

DROP INDEX lvl_name_program_order_idx;

-- There could be scenarios where the previous unique index is unable to be made.  A non-unique index with the same name
-- will allow the existing migration to run again in the event of an undo.
CREATE INDEX lvl_name_program_id_idx ON observation_unit_level_name (level_name, program_id);