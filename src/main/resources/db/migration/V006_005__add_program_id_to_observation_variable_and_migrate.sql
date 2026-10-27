ALTER TABLE observation_variable
    ADD COLUMN program_id UUID;

ALTER TABLE observation_variable
    ADD CONSTRAINT observation_variable_program_fk
        FOREIGN KEY (program_id)
            REFERENCES public.program(id);

CREATE INDEX observation_var_program_idx ON observation_variable (program_id, id);

UPDATE observation_variable set program_id = pquery.program_id
    FROM (
        SELECT ov.id AS obs_var_id, p.id AS program_id
        FROM observation_variable ov
        JOIN variable_base_entity_external_references ovex ON ov.id = ovex.variable_base_entity_id
        JOIN external_reference ex ON ex.id = ovex.external_references_id
        JOIN external_reference ex2 ON ex2.external_reference_id = ex.external_reference_id
        JOIN program_external_references pex ON pex.external_references_id = ex2.id
        JOIN program p ON p.id = pex.program_entity_id
        WHERE ex.external_reference_source = 'breedinginsight.org/programs' AND ex2.external_reference_source = 'breedinginsight.org') pquery
WHERE id = pquery.obs_var_id;

-- Assertion to ensure all observation variables are now associated with a program
DO $$
    DECLARE
obs_vars_without_program_count integer;

BEGIN
SELECT COUNT(*)
INTO obs_vars_without_program_count
FROM observation_variable ov
WHERE ov.program_id IS NULL;

IF obs_vars_without_program_count <> 0 THEN
                RAISE EXCEPTION
                    'V006.005 After migration, expected all observation_variable rows to have program_id set, but there are % rows with program_id not set',
                    obs_vars_without_program_count;
END IF;
END
$$;