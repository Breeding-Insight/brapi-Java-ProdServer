WITH
    levels_and_parent_ou_bi_id AS (
--      Find observation_unit_level codes that contain a key by checking for a space in the level code.
--      Only other level codes that can exist are block/rep.
--      Then extract the uuid from the level code, since program key is also contained there.
        SELECT
            id AS level_id,
            substring(level_code FROM '^([^ ]+)') AS parent_bi_ou_id
        FROM observation_unit_level
        WHERE level_code LIKE '% %'
    ),
    -- This query should pick up external references with a unique external_reference_id and external_reference_source.
    -- This is critical for this update as if there are any external references used that are not unique,
    -- we could assign the wrong observation_unit_id.
    exrefid_source_with_one_ou_connected AS (
        SELECT ex.external_reference_id, ex.external_reference_source, (array_agg(id))[1] AS exref_pk
FROM external_reference ex
    JOIN observation_unit_external_references ouex ON ex.id = ouex.external_references_id
WHERE ex.external_reference_source = 'breedinginsight.org/observationunits'
GROUP BY ex.external_reference_id, ex.external_reference_source
HAVING count(*) = 1
    ),
    ou_ids_matched_on_levels AS (
--      Now match the bi-generated exref ou ids to ex refs ids, and keep observation_unit_level ids for matching in next part
SELECT
    ou.id AS ou_id,
    levels_and_parent_ou_bi_id.level_id
FROM observation_unit ou
    JOIN observation_unit_external_references ouex ON ou.id = ouex.observation_unit_entity_id
    JOIN exrefid_source_with_one_ou_connected ON exrefid_source_with_one_ou_connected.exref_pk = ouex.external_references_id
    JOIN levels_and_parent_ou_bi_id ON exrefid_source_with_one_ou_connected.external_reference_id = levels_and_parent_ou_bi_id.parent_bi_ou_id
    )
UPDATE observation_unit_level
SET level_code = regexp_replace(
        observation_unit_level.level_code,
        '^[^ ]+',
        mol.ou_id::text
)
FROM ou_ids_matched_on_levels mol
WHERE observation_unit_level.id = mol.level_id;

-- Assertion to assure all expected top-level observation unit db ids were changed from external reference ids to observation unit db ids
DO $$
DECLARE
    top_level_observation_unit_level_count integer;
    top_level_ouls_matched_to_ou_id integer;
BEGIN
    SELECT COUNT(*)
    INTO top_level_observation_unit_level_count
    FROM observation_unit_level
    WHERE level_code like '% %';

    SELECT COUNT(*)
    INTO top_level_ouls_matched_to_ou_id
    FROM observation_unit_level oul
    JOIN observation_unit ou on ou.id::text = substring(level_code FROM '^([^ ]+)')
    WHERE level_code like '% %';

    IF top_level_observation_unit_level_count <> top_level_ouls_matched_to_ou_id THEN
        RAISE EXCEPTION
          'V006.001 After migration, expected all % observation_unit_level.code rows to match to observation_unit.id, but only % matched',
            top_level_observation_unit_level_count,
            top_level_ouls_matched_to_ou_id;
    END IF;
END $$;