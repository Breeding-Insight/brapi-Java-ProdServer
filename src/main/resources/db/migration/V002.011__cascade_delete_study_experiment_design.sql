-- This migration will delete records associated with a deleted study

-- First, drop the existing foreign key constraints
ALTER TABLE ONLY public.study_experimental_design
DROP CONSTRAINT IF EXISTS fk6p1eig0ibi880tu9yr3vd8yfn;

ALTER TABLE ONLY public.study_environment_parameter
DROP CONSTRAINT IF EXISTS fkhkcbdh997uw56v7orukh2hssm;

ALTER TABLE ONLY public.study_data_link
DROP CONSTRAINT IF EXISTS fknhgb2dwb4ffbsmydrhmephyyd;

ALTER TABLE ONLY public.study_contact
DROP CONSTRAINT IF EXISTS fkojmdovj11j3s8qxewlfjuw08y;

ALTER TABLE ONLY public.study_growth_facility
DROP CONSTRAINT IF EXISTS fkhie58olkxqm2t1ht6k5hl4wu5;

ALTER TABLE ONLY public.study_last_update
DROP CONSTRAINT IF EXISTS fkogmwhwbr1qx6vr1qr1nh6owpp;

ALTER TABLE ONLY public.study_observation_level
DROP CONSTRAINT IF EXISTS fkq886b0f9213w91opmlha96b0g;

ALTER TABLE ONLY public.study_variable
DROP CONSTRAINT IF EXISTS fkdg1qfle74hjs7s6a31j6poxs;

-- Then, recreate the constraints with ON DELETE CASCADE
ALTER TABLE ONLY public.study_experimental_design
    ADD CONSTRAINT fk6p1eig0ibi880tu9yr3vd8yfn
    FOREIGN KEY (study_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study_environment_parameter
    ADD CONSTRAINT fkhkcbdh997uw56v7orukh2hssm
    FOREIGN KEY (study_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study_data_link
    ADD CONSTRAINT fknhgb2dwb4ffbsmydrhmephyyd
    FOREIGN KEY (study_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study_contact
    ADD CONSTRAINT fkojmdovj11j3s8qxewlfjuw08y
    FOREIGN KEY (study_db_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study_growth_facility
    ADD CONSTRAINT fkhie58olkxqm2t1ht6k5hl4wu5
    FOREIGN KEY (study_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study_last_update
    ADD CONSTRAINT fkogmwhwbr1qx6vr1qr1nh6owpp
    FOREIGN KEY (study_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study_observation_level
    ADD CONSTRAINT fkq886b0f9213w91opmlha96b0g
    FOREIGN KEY (study_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study_variable
    ADD CONSTRAINT fkdg1qfle74hjs7s6a31j6poxs
    FOREIGN KEY (study_db_id)
    REFERENCES public.study(id)
    ON DELETE CASCADE;