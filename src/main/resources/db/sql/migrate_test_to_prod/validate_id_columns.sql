CREATE OR REPLACE FUNCTION check_invalid_uuids() RETURNS void AS $$
DECLARE
    table_record RECORD;
    column_record RECORD;
    has_invalid_uuid BOOLEAN;
BEGIN
    -- Loop through all tables that may contain the 'id' column
    FOR table_record IN
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = 'public' AND table_type = 'BASE TABLE'
    LOOP
        BEGIN
            -- Try to find the column name that holds the ID
            FOR column_record IN
                SELECT column_name
                FROM information_schema.columns
                WHERE table_name = table_record.table_name AND column_name like '%id%' AND data_type = 'text'
            LOOP
                -- Use EXISTS to check if any invalid UUIDs exist
                EXECUTE format('
                    SELECT EXISTS (
                        SELECT 1
                        FROM %I
                        WHERE %I !~ ''^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$''
                        LIMIT 1
                    )',
                    table_record.table_name, column_record.column_name)
                INTO has_invalid_uuid;

                -- If invalid UUIDs are detected, raise a notice
                IF has_invalid_uuid THEN
                    RAISE NOTICE 'Table %: Invalid UUID detected in column %',
                        table_record.table_name, column_record.column_name;
                END IF;
            END LOOP;
        EXCEPTION WHEN OTHERS THEN
            RAISE NOTICE 'Skipping table % due to error: %', table_record.table_name, SQLERRM;
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;