ALTER TABLE germplasm
ADD COLUMN program_id UUID;

ALTER TABLE germplasm
ADD CONSTRAINT germplasm_program_fk
    FOREIGN KEY (program_id)
    REFERENCES public.program(id);

CREATE INDEX germplasm_program_idx ON germplasm (program_id, id);

UPDATE germplasm set program_id = pquery.program_id
FROM (
    SELECT g.id AS germplasm_id, p.id AS program_id
    FROM germplasm g
             JOIN germplasm_external_references gex ON g.id = gex.germplasm_entity_id
             JOIN external_reference ex ON ex.id = gex.external_references_id
             JOIN external_reference ex2 ON ex2.external_reference_id = ex.external_reference_id
             JOIN program_external_references pex ON pex.external_references_id = ex2.id
             JOIN program p ON p.id = pex.program_entity_id
    WHERE ex.external_reference_source = 'breedinginsight.org/programs' AND ex2.external_reference_source = 'breedinginsight.org') pquery
WHERE id = pquery.germplasm_id;

-- Assertion to ensure all germplasm are now associated with a program
DO $$
    DECLARE
        germs_without_program_count integer;

    BEGIN
        SELECT COUNT(*)
        INTO germs_without_program_count
        FROM germplasm g
        WHERE g.program_id IS NULL;

        IF germs_without_program_count <> 0 THEN
                RAISE EXCEPTION
                    'V006.003 After migration, expected all germplasm rows to have program_id set, but there are % rows with program_id not set',
                    germs_without_program_count;
        END IF;
    END
$$;