-- Rename 'public.batch' table to 'public.batch_delete' and its columns
ALTER TABLE public.batch RENAME TO batch_delete;
ALTER TABLE public.batch_delete RENAME COLUMN batch_name TO batch_delete_name;
ALTER TABLE public.batch_delete RENAME COLUMN batch_owner_name TO batch_delete_owner_name;
ALTER TABLE public.batch_delete RENAME COLUMN batch_source TO batch_delete_source;
ALTER TABLE public.batch_delete RENAME COLUMN batch_type TO batch_delete_type;
ALTER TABLE public.batch_delete RENAME COLUMN batch_owner_person_id TO batch_delete_owner_person_id;

-- Rename 'public.batch_external_references' table to 'public.batch_delete_external_references' and its column
ALTER TABLE public.batch_external_references RENAME TO batch_delete_external_references;
ALTER TABLE public.batch_delete_external_references RENAME COLUMN batch_entity_id TO batch_delete_entity_id;

-- Rename 'public.batch_item' table to 'public.batch_delete_item' and its column
ALTER TABLE public.batch_item RENAME TO batch_delete_item;
ALTER TABLE public.batch_delete_item RENAME COLUMN batch_id TO batch_delete_id;




