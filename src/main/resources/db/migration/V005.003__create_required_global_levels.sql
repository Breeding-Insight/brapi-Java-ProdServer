-- Add rep and block global levels needed by DeltaBreed if they don't exist

INSERT INTO observation_unit_level_name (level_name, level_order)
VALUES
    ('rep', 3),
    ('block', 4)
ON CONFLICT (level_name) WHERE program_id IS NULL DO NOTHING;
