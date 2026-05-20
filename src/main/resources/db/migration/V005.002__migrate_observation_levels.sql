-- Create new observation level names for each unique observationLevel in
-- the additional_info column of the observation_unit table
--
-- Assumption: all observation_unit.program_id have ids which is true for our
-- usage. Database allows NULLs but we don't have to handle that
--
-- * The level order will be 0 for all of these indicating Exp Unit
-- * All level names should be converted to lower case and combined
--   in the case of different cases to reference the same level
-- * Level names should be created for the program the observation unit is in, not globally
INSERT INTO observation_unit_level_name (level_name, level_order, program_id)
SELECT DISTINCT
    lower(btrim(ou.additional_info ->> 'observationLevel')) AS level_name,
    0 AS level_order,
    ou.program_id
FROM observation_unit ou
WHERE NULLIF(btrim(ou.additional_info ->> 'observationLevel'), '') IS NOT NULL
  AND ou.program_id IS NOT NULL
    ON CONFLICT (level_name, program_id) DO NOTHING;
-- on conflict only relevant for test servers, won't have conflicts on prod because
-- no additional observationlevelnames have been created other than global defaults from migration

-- Update global level references to newly created program scoped levels
-- For prod, these will be:
-- * plot
-- * plant
-- * fish
-- * bine
-- * tree
-- There will be multiple instances as some levels are reused in different programs
--
-- Need to handle observation_unit_position, but can skip & observation_unit_level and
-- study_observation_level. bi-api has never written study_observation_level and no records exist
-- in prod. observation_unit_level only has rep and block global references and those were already
-- updated in a previous migration.

-- Updates observation_unit_position.level_name to point to the program-scoped
-- observation_unit_level_name record that matches the observation unit's
-- additional_info.observationLevel value.
--
-- Only updates rows where:
-- * additional_info.observationLevel is present and not blank
-- * observation_unit.program_id is present
-- * the current linked level is null or does not already match the target
--   * some data like tree will point to plot due to how old data was written and will be fixed by this
--   * some data has null levels due to how it was written and will be fixed by this
WITH rows_to_update AS (
    SELECT
        oup.id AS observation_unit_position_id,
        target.id AS target_level_id
    FROM observation_unit_position oup
             JOIN observation_unit ou
                  ON ou.id = oup.observation_unit_id
             JOIN observation_unit_level_name target
                  ON target.program_id = ou.program_id
                      AND target.level_name = lower(btrim(ou.additional_info ->> 'observationLevel'))
    WHERE NULLIF(btrim(ou.additional_info ->> 'observationLevel'), '') IS NOT NULL
      AND ou.program_id IS NOT NULL
      AND oup.level_name IS DISTINCT FROM target.id
    )
UPDATE observation_unit_position oup
SET level_name = rows_to_update.target_level_id
    FROM rows_to_update
WHERE oup.id = rows_to_update.observation_unit_position_id;

-- Assertion to assure we don't remove additional_info.observationLevel where
-- references can't be updated. For our prod data, shouldn't be any.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM observation_unit ou
        WHERE NULLIF(btrim(ou.additional_info ->> 'observationLevel'), '') IS NOT NULL
            AND (
                ou.program_id IS NULL
                OR NOT EXISTS (
                    SELECT 1
                    FROM observation_unit_level_name ouln
                    WHERE ouln.program_id = ou.program_id
                        AND ouln.level_name = lower(btrim(ou.additional_info ->> 'observationLevel'))
                )
            )
    ) THEN
    RAISE EXCEPTION
          'V005.002 cannot remove observationLevel from observation_unit.additional_info because some rows could not be
    mapped';
END IF;
END $$;

-- Remove observationLevel from additional_info in observation_unit table as it is no longer needed
UPDATE observation_unit
SET additional_info = additional_info - 'observationLevel';

-- Remove unused global level names (will exclude rep & block). On prod, these will be removed:
-- * entry
-- * field
-- * pot
-- * sample
-- * study
-- * sub-block
-- * sub-plot
-- Level is unused if not referenced in tables:
-- * observation_unit_position
-- * observation_unit_level
-- * study_observation_level has no existing data in prod but included
DELETE FROM observation_unit_level_name ouln
WHERE ouln.program_id IS NULL
    AND NOT EXISTS (
        SELECT 1 FROM observation_unit_position oup WHERE oup.level_name = ouln.id
    )
    AND NOT EXISTS (
        SELECT 1 FROM observation_unit_level oul WHERE oul.level_name = ouln.id
    )
    AND NOT EXISTS (
        SELECT 1 FROM study_observation_level sol WHERE sol.level_name = ouln.id
    );
