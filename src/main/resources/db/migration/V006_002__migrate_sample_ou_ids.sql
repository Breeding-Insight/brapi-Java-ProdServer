WITH
    samples_and_bi_ou_ids AS (
--      Look for samples that have obsUnitIDs and keep ids in hand for next query
        SELECT
            id as sample_id,
            additional_info ->> 'obsUnitID' AS bi_ou_id
FROM sample
WHERE additional_info ? 'obsUnitID'
    ),
    ou_ids_matched_on_samples AS (
--      Now match the bi-generated exref ou ids to ex refs ids, and keep samples ids for matching in update
SELECT
    ou.id AS ou_id,
    samples_and_bi_ou_ids.sample_id
FROM observation_unit ou
    JOIN observation_unit_external_references ouex ON ou.id = ouex.observation_unit_entity_id
    JOIN external_reference ex ON ouex.external_references_id = ex.id
    JOIN samples_and_bi_ou_ids ON ex.external_reference_id = samples_and_bi_ou_ids.bi_ou_id
    )
UPDATE sample
SET additional_info = jsonb_set(
        additional_info,
        '{obsUnitID}',
        to_jsonb(mos.ou_id)
                      )
    FROM ou_ids_matched_on_samples mos
WHERE id = mos.sample_id