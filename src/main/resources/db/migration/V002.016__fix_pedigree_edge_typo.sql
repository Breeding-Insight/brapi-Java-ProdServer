-- V2__fix_typo_in_column_name.sql
-- Flyway migration script to fix the typo in the column name "connceted_node_id"

-- Rename the column
ALTER TABLE public.pedigree_edge
    RENAME COLUMN connceted_node_id TO connected_node_id;

-- Drop the old index (it references the old column name)
DROP INDEX IF EXISTS public.pedigree_edge_connected_node_id;

-- Recreate the index with the correct column name
CREATE INDEX pedigree_edge_connected_node_id ON public.pedigree_edge USING btree (connected_node_id);

-- Drop the old foreign key constraint (it references the old column name)
ALTER TABLE ONLY public.pedigree_edge
DROP CONSTRAINT IF EXISTS fk5ds4uo4f3tsurnjtc8dxb7w6r;

-- Add a new foreign key constraint with the correct column name
ALTER TABLE ONLY public.pedigree_edge
    ADD CONSTRAINT fk5ds4uo4f3tsurnjtc8dxb7w6r
    FOREIGN KEY (connected_node_id)
    REFERENCES public.pedigree_node(id)
    ON DELETE CASCADE;
