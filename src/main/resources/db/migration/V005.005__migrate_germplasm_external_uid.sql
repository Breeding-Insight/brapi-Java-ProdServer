-- Update germplasm exref sources to 'External UID' instead of
-- seed source value for external UIDs.

UPDATE external_reference exref
SET external_reference_source = 'External UID'
WHERE exref.external_reference_source IS DISTINCT FROM 'External UID'
  AND NULLIF(btrim(exref.external_reference_source), '') IS NOT NULL
  AND EXISTS (
    SELECT 1
    FROM germplasm_external_references ger
      JOIN germplasm g
        ON g.id = ger.germplasm_entity_id
    WHERE ger.external_references_id = exref.id
      AND NULLIF(btrim(g.seed_source), '') IS NOT NULL
      AND btrim(g.seed_source) = btrim(exref.external_reference_source)
  );
