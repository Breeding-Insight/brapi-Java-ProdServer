-- This migration will delete records associated with a deleted trial

-- First, drop the existing foreign key constraint
ALTER TABLE ONLY public.observation_unit
DROP CONSTRAINT IF EXISTS fk1bj7oq9sdtdy9sy73gj3lvxrg;

ALTER TABLE ONLY public.trial_publication
DROP CONSTRAINT IF EXISTS fk3mtl83xv18xwseiuvrog2crft;

ALTER TABLE ONLY public.sample
DROP CONSTRAINT IF EXISTS fk8p8p683y5axjauvbt4bgp0lly;

ALTER TABLE ONLY public.study
DROP CONSTRAINT IF EXISTS fk9voefp8el048ula850imkkuiq;

ALTER TABLE ONLY public.observation
DROP CONSTRAINT IF EXISTS fkcviusg5q0avmk5xv7nix9gxh0;

ALTER TABLE ONLY public.trial_dataset_authorship
DROP CONSTRAINT IF EXISTS fkd13q53ipjrr22rkelycgxjio4;

ALTER TABLE ONLY public.trial_contact
DROP CONSTRAINT IF EXISTS fkeqh9256ey5a136uatx4hu6gc4;

ALTER TABLE ONLY public.plate
DROP CONSTRAINT IF EXISTS fkky4n35tyqgvf2hwq6958yo765;

-- Then, recreate the constraint with ON DELETE CASCADE
ALTER TABLE ONLY public.observation_unit
    ADD CONSTRAINT fk1bj7oq9sdtdy9sy73gj3lvxrg
        FOREIGN KEY (trial_id)
            REFERENCES public.trial(id)
            ON DELETE CASCADE;

ALTER TABLE ONLY public.trial_publication
    ADD CONSTRAINT fk3mtl83xv18xwseiuvrog2crft
    FOREIGN KEY (trial_id)
    REFERENCES public.trial(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.sample
    ADD CONSTRAINT fk8p8p683y5axjauvbt4bgp0lly
    FOREIGN KEY (trial_id)
    REFERENCES public.trial(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.study
    ADD CONSTRAINT fk9voefp8el048ula850imkkuiq
    FOREIGN KEY (trial_id)
    REFERENCES public.trial(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.observation
    ADD CONSTRAINT fkcviusg5q0avmk5xv7nix9gxh0
    FOREIGN KEY (trial_id)
    REFERENCES public.trial(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.trial_dataset_authorship
    ADD CONSTRAINT fkd13q53ipjrr22rkelycgxjio4
    FOREIGN KEY (trial_id)
    REFERENCES public.trial(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.trial_contact
    ADD CONSTRAINT fkeqh9256ey5a136uatx4hu6gc4
    FOREIGN KEY (trial_db_id)
    REFERENCES public.trial(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.plate
    ADD CONSTRAINT fkky4n35tyqgvf2hwq6958yo765
    FOREIGN KEY (trial_id)
    REFERENCES public.trial(id)
    ON DELETE CASCADE;