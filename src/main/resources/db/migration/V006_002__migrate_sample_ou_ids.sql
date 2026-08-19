WITH
    samples_and_bi_ou_ids AS (
--      Look for samples that have obsUnitIDs and keep ids in hand for next query
        SELECT
            id as sample_id,
            additional_info ->> 'obsUnitID' AS bi_ou_id
        FROM sample
        WHERE additional_info ? 'obsUnitID'
    ),
    exrefid_source_with_one_ou_connected AS (
        SELECT ex.external_reference_id, ex.external_reference_source, (array_agg(id))[1] AS exref_pk
        FROM external_reference ex
        JOIN observation_unit_external_references ouex ON ex.id = ouex.external_references_id
        WHERE ex.external_reference_source = 'breedinginsight.org/observationunits'
        GROUP BY ex.external_reference_id, ex.external_reference_source
        HAVING COUNT(*) = 1
    ),
    ou_ids_matched_on_samples AS (
--      Now match the bi-generated exref ou ids to ex refs ids, and keep samples ids for matching in update
        SELECT
            ou.id AS ou_id,
            samples_and_bi_ou_ids.sample_id
        FROM observation_unit ou
        JOIN observation_unit_external_references ouex ON ou.id = ouex.observation_unit_entity_id
        JOIN exrefid_source_with_one_ou_connected on exrefid_source_with_one_ou_connected.exref_pk = ouex.external_references_id
        JOIN samples_and_bi_ou_ids ON exrefid_source_with_one_ou_connected.external_reference_id = samples_and_bi_ou_ids.bi_ou_id
    )
UPDATE sample
SET additional_info = jsonb_set(
        additional_info,
        '{obsUnitID}',
        to_jsonb(mos.ou_id)
)
FROM ou_ids_matched_on_samples mos
WHERE id = mos.sample_id;

-- Assertion to ensure all samples with ou in additional info now relate to observation unit db id
DO $$
    DECLARE
    samples_with_ou_count integer;
            samples_with_ous_matched_to_ou_count integer;
    BEGIN
        SELECT COUNT(*)
        INTO samples_with_ou_count
        FROM sample
        WHERE additional_info ? 'obsUnitID';

        SELECT COUNT(*)
        INTO samples_with_ous_matched_to_ou_count
        FROM sample s
                 JOIN observation_unit ou on ou.id::text = s.additional_info ->> 'obsUnitID'
        WHERE s.additional_info ? 'obsUnitID';

        IF samples_with_ou_count <> samples_with_ous_matched_to_ou_count THEN
                    RAISE EXCEPTION
                        'V006.002 After migration, expected all % sample.additional_info->>obsUnitID rows to match to observation_unit.id, but only % matched',
                        samples_with_ou_count,
                        samples_with_ous_matched_to_ou_count;
        END IF;
    END
$$;