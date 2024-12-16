-- Drop the trigger if it exists
DROP TRIGGER IF EXISTS sync_soft_deleted_status ON public.list;

-- Drop the function if it exists
DROP FUNCTION IF EXISTS sync_list_related_tables_soft_deleted();

-- Remove soft_deleted column from list table if it exists
ALTER TABLE public.list
DROP COLUMN IF EXISTS soft_deleted;

-- Remove soft_deleted column from list_external_references table if it exists
ALTER TABLE public.list_external_references
DROP COLUMN IF EXISTS soft_deleted;

-- Remove soft_deleted column from list_item table if it exists
ALTER TABLE public.list_item
DROP COLUMN IF EXISTS soft_deleted;

-- Add soft_deleted column to list, list_external_references, and list_item tables
ALTER TABLE public.list
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.list_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.list_item
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Create a trigger function to update soft_deleted status
CREATE OR REPLACE FUNCTION sync_list_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
    -- Update list_external_references
UPDATE public.list_external_references
SET soft_deleted = NEW.soft_deleted
WHERE list_entity_id = NEW.id;

-- Update list_item
UPDATE public.list_item
SET soft_deleted = NEW.soft_deleted
WHERE list_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger on the list table
CREATE TRIGGER sync_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.list
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_list_related_tables_soft_deleted();


