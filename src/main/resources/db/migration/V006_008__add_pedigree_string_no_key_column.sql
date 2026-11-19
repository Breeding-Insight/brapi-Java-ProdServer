ALTER TABLE pedigree_node
    ADD COLUMN
        pedigree_string_no_key text COLLATE natural_sort
            GENERATED ALWAYS AS (regexp_replace(pedigree_string, ' \[[^\[\]]*\]', '', 'g')) STORED;
