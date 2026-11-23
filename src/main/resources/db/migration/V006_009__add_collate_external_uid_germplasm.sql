ALTER TABLE germplasm
    ADD COLUMN
        external_uid text NOT NULL DEFAULT '' COLLATE natural_sort;

UPDATE germplasm SET external_uid = exuidquery.external_uid
    FROM (
        SELECT ex.external_reference_id AS external_uid, g.id AS germplasm_id
        FROM germplasm g
        JOIN germplasm_external_references gex ON gex.germplasm_entity_id = g.id
        JOIN external_reference ex ON ex.id = gex.external_references_id
WHERE ex.external_reference_source = 'External UID') exuidquery
WHERE id = exuidquery.germplasm_id;