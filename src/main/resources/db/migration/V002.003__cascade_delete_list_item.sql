-- This migration will delete list_item records associated with a list that
-- is deleted.

-- First, drop the existing foreign key constraint
ALTER TABLE public.list_item
DROP CONSTRAINT IF EXISTS fk1ddq3ct1ulogjn5ijs8ert7hw;

-- Then, recreate the constraint with ON DELETE CASCADE
ALTER TABLE public.list_item
    ADD CONSTRAINT fk1ddq3ct1ulogjn5ijs8ert7hw
        FOREIGN KEY (list_id)
            REFERENCES public.list(id)
            ON DELETE CASCADE;
