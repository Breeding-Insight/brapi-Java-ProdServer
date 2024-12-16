-- This migration will delete records associated with a deleted sample

-- First, drop the existing foreign key constraint
ALTER TABLE ONLY public.vendor_file_sample
DROP CONSTRAINT IF EXISTS fke3tnyn895kve2kgixku4j7htb;

ALTER TABLE ONLY public.callset
DROP CONSTRAINT IF EXISTS fkhreq22htrftm3dul7nfsg1agk;

-- Then, recreate the constraint with ON DELETE CASCADE
ALTER TABLE ONLY public.vendor_file_sample
    ADD CONSTRAINT fke3tnyn895kve2kgixku4j7htb
        FOREIGN KEY (sample_dbid)
            REFERENCES public.sample(id)
            ON DELETE CASCADE;

ALTER TABLE ONLY public.callset
    ADD CONSTRAINT fkhreq22htrftm3dul7nfsg1agk
    FOREIGN KEY (sample_id)
    REFERENCES public.sample(id)
    ON DELETE CASCADE;