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
    ou_ids_matched_on_levels AS (
--      Now match the bi-generated exref ou ids to ex refs ids, and keep observation_unit_level ids for matching in next part
        SELECT
            ou.id AS ou_id,
            levels_and_parent_ou_bi_id.level_id
        FROM observation_unit ou
        JOIN observation_unit_external_references ouex ON ou.id = ouex.observation_unit_entity_id
        JOIN external_reference ex ON ouex.external_references_id = ex.id
        JOIN levels_and_parent_ou_bi_id ON ex.external_reference_id = levels_and_parent_ou_bi_id.parent_bi_ou_id
    )
UPDATE observation_unit_level
SET level_code = regexp_replace(
        observation_unit_level.level_code,
        '^[^ ]+',
        mol.ou_id::text
)
FROM ou_ids_matched_on_levels mol
WHERE observation_unit_level.id = mol.level_id