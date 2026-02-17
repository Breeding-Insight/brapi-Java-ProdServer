WITH treatment_json AS (SELECT
                            observation_unit_id,
                            jsonb_agg(
                                    jsonb_build_object('factor', factor)
                            ) AS treatments
                        FROM observation_unit_treatment
                        GROUP BY observation_unit_id)

UPDATE observation_unit ou
SET additional_info =
        jsonb_set(
                COALESCE(ou.additional_info, '{}'::jsonb),
                '{treatments}',
                tj.treatments,
                true
        )
    FROM treatment_json tj
WHERE ou.id = tj.observation_unit_id;