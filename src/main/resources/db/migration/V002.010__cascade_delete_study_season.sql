-- This migration will delete records associated with a deleted study

-- First, drop the existing foreign key constraint
ALTER TABLE ONLY public.study_season
DROP CONSTRAINT IF EXISTS fk1r2518mglhwijy5mk31cfxh3h;

-- Then, recreate the constraint with ON DELETE CASCADE
ALTER TABLE ONLY public.study_season
    ADD CONSTRAINT fk1r2518mglhwijy5mk31cfxh3h
        FOREIGN KEY (study_db_id)
            REFERENCES public.study(id)
            ON DELETE CASCADE;
