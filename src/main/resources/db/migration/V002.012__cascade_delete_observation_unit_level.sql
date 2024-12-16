-- This migration will cascade delete observation unit levels upon deletion of a referenced unit position

-- First, drop the existing foreign key constraints
ALTER TABLE ONLY public.observation_unit_level
DROP CONSTRAINT IF EXISTS fkh70bs6tax01tl04ha9qjqo19;


-- Then, recreate the constraints with ON DELETE CASCADE
ALTER TABLE ONLY public.observation_unit_level
    ADD CONSTRAINT fkh70bs6tax01tl04ha9qjqo19
    FOREIGN KEY (position_id)
    REFERENCES public.observation_unit_position(id)
    ON DELETE CASCADE;