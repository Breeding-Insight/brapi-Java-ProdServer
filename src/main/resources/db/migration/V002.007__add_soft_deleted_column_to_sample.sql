-- Add soft_deleted column
ALTER TABLE ONLY public.sample
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE ONLY public.sample_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE ONLY public.vendor_file_sample
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE ONLY public.callset
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Create a trigger function to update soft_deleted status
CREATE OR REPLACE FUNCTION sync_sample_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
    -- Update sample_external_references
UPDATE public.sample_external_references
SET soft_deleted = NEW.soft_deleted
WHERE sample_entity_id = NEW.id;

-- Update vendor_file_sample
UPDATE public.vendor_file_sample
SET soft_deleted = NEW.soft_deleted
WHERE sample_dbid = NEW.id;

-- Update callset
UPDATE public.callset
SET soft_deleted = NEW.soft_deleted
WHERE sample_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger on the sample table
CREATE TRIGGER sync_sample_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.sample
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_sample_related_tables_soft_deleted();


