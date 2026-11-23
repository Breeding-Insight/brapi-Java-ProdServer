-- trial_name without the trailing program key " [KEY]", used only for sorting/filtering.
-- trial_name itself is left unchanged.
ALTER TABLE trial
    ADD COLUMN
        trial_name_no_key text NOT NULL COLLATE natural_sort
            GENERATED ALWAYS AS (regexp_replace(trial_name, ' \[[^\[\]]*\]$', '')) STORED;
