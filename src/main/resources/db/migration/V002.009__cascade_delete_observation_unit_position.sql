-- This migration will delete an observation unit position that references a deleted observation unit

-- First, drop the existing foreign key constraint
ALTER TABLE ONLY public.observation_unit_position
DROP CONSTRAINT IF EXISTS fk2mpgvaq1ppb8kjp3jk0ecpdyr;

-- Then, recreate the constraint with ON DELETE CASCADE
ALTER TABLE ONLY public.observation_unit_position
    ADD CONSTRAINT fk2mpgvaq1ppb8kjp3jk0ecpdyr
         FOREIGN KEY (observation_unit_id)
             REFERENCES public.observation_unit(id)
             ON DELETE CASCADE;
