
-- This migration updates existing list_item records based on DeltaBreed (Breeding Insight) specific fields.
--
-- These are the list types, the BJTS uses Java enums and stores ints in the database.
-- 0: germplasm
-- 1: markers
-- 2: programs
-- 3: trials
-- 4: studies
-- 5: observationUnits
-- 6: observations
-- 7: observationVariables
-- 8: samples

DO
$$
BEGIN
    -- Update germplasm list items, the goal is to use the order defined by the listEntryNumbers.
    UPDATE
        list_item
    SET
        position = subquery.position
    FROM
        (
            SELECT
                -- Subtract 1 from row_number to get zero indexing.
                row_number() OVER (PARTITION BY li.list_id ORDER BY (g.additional_info->'listEntryNumbers'->>xr.external_reference_id::text)::int) - 1 AS position,
                li.id AS list_item_id
            FROM
                list_item li
                    JOIN list l ON li.list_id = l.id
                    JOIN list_external_references ler ON l.id = ler.list_entity_id
                    JOIN external_reference xr ON xr.id = ler.external_references_id AND xr.external_reference_source = 'breedinginsight.org/lists'
                    JOIN germplasm g ON li.item = g.germplasm_name
            WHERE
                l.list_type = 0  -- 0 is germplasm
            ORDER BY
                l.id
        ) AS subquery
    WHERE
        list_item.id = subquery.list_item_id
    ;

    -- Update all non-germplasm list items. There is no existing order to preserve, assign sequential position values arbitrarily.
    UPDATE
        list_item
    SET
        position = subquery.position
    FROM
        (
            SELECT
                -- Subtract 1 from row_number to get zero indexing.
                row_number() OVER (PARTITION BY li.list_id) - 1 AS position,
                li.id AS list_item_id
            FROM
                list_item li
                JOIN list l ON li.list_id = l.id
            WHERE
                l.list_type != 0  -- 0 is germplasm, here we are addressing non-germplasm lists.
            ORDER BY
                l.id
        ) AS subquery
    WHERE
        list_item.id = subquery.list_item_id
    ;

END;
$$;
