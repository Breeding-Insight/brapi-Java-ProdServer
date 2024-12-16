-- Add soft_deleted column to trial
ALTER TABLE public.trial
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.trial_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.observation_unit
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.trial_publication
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.study
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.observation
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.trial_dataset_authorship
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.trial_contact
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.plate
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Create a trigger function to update soft_deleted status
CREATE OR REPLACE FUNCTION sync_trial_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
    -- Update trial_external_references
UPDATE public.trial_external_references
SET soft_deleted = NEW.soft_deleted
WHERE trial_entity_id = NEW.id;

-- Update observation_unit
UPDATE public.observation_unit
SET soft_deleted = NEW.soft_deleted
WHERE trial_id = NEW.id;

-- Update trial_publication
UPDATE public.trial_publication
SET soft_deleted = NEW.soft_deleted
WHERE trial_id = NEW.id;

-- Update sample
UPDATE public.sample
SET soft_deleted = NEW.soft_deleted
WHERE trial_id = NEW.id;

-- Update study
UPDATE public.study
SET soft_deleted = NEW.soft_deleted
WHERE trial_id = NEW.id;

-- Update observation
UPDATE public.observation
SET soft_deleted = NEW.soft_deleted
WHERE trial_id = NEW.id;

-- Update trial_dataset_authorship
UPDATE public.trial_dataset_authorship
SET soft_deleted = NEW.soft_deleted
WHERE trial_id = NEW.id;

-- Update trial_contact
UPDATE public.trial_contact
SET soft_deleted = NEW.soft_deleted
WHERE trial_db_id = NEW.id;

-- Update plate
UPDATE public.plate
SET soft_deleted = NEW.soft_deleted
WHERE trial_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger on the list table
CREATE TRIGGER sync_trial_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.trial
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_trial_related_tables_soft_deleted();


