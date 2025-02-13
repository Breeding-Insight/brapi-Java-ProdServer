-- Add soft_deleted column to germplasm
ALTER TABLE public.germplasm
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_attribute_value
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_attribute_value_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_donor
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_donor_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_entity_type_of_germplasm_storage_code
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_institute
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_origin
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_search_results
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_synonym
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.germplasm_taxon
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.pedigree_node
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.pedigree_node_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.pedigree_edge
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.pedigree_edge_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.cross_parent
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE table_name = 'observation_unit'
          AND table_schema = 'public'
          AND column_name = 'soft_deleted'
    ) THEN
ALTER TABLE public.observation_unit
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;
END IF;
END $$;

ALTER TABLE public.observation_unit_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.observation_unit_level
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.observation_unit_position
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.observation_unit_treatment
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.event_observation_units
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE table_name = 'observation'
          AND table_schema = 'public'
          AND column_name = 'soft_deleted'
    ) THEN
ALTER TABLE public.observation
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;
END IF;
END $$;

ALTER TABLE public.image
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.image_entity_descriptive_ontology_terms
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.image_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.image_observations
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.reference_set
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.reference_set_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.reference
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.reference_bases
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.reference_bases_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.reference_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.seed_lot_content_mixture
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variant
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variant_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variant_entity_alternate_bases
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variant_entity_ciend
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variant_entity_cipos
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variant_entity_filters_failed
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variantset
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variantset_analysis
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variantset_external_references
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE public.variantset_format
    ADD COLUMN soft_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Create a trigger function to update soft_deleted status for records with germplasm foreign key
CREATE OR REPLACE FUNCTION sync_germplasm_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update germplasm_external_references
UPDATE public.germplasm_external_references
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_entity_id = NEW.id;

-- Update germplasm_attribute_value
UPDATE public.germplasm_attribute_value
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update germplasm_donor
UPDATE public.germplasm_donor
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update germplasm_entity_type_of_germplasm_storage_code
UPDATE public.germplasm_entity_type_of_germplasm_storage_code
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_entity_id = NEW.id;

-- Update germplasm_institute
UPDATE public.germplasm_institute
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update germplasm_origin
UPDATE public.germplasm_origin
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update germplasm_search_results
UPDATE public.germplasm_search_results
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_entity_id = NEW.id;

-- Update germplasm_synonyms
UPDATE public.germplasm_synonym
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update germplasm_taxon
UPDATE public.germplasm_taxon
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update pedigree_node
UPDATE public.pedigree_node
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update cross_parent
UPDATE public.cross_parent
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update observation_unit
UPDATE public.observation_unit
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

-- Update reference_set
UPDATE public.reference_set
SET soft_deleted = NEW.soft_deleted
WHERE source_germplasm_id = NEW.id;

-- Update seed_lot_content_mixture
UPDATE public.seed_lot_content_mixture
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with germplasm_attribute_value foreign key
CREATE OR REPLACE FUNCTION sync_germplasm_attribute_value_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update germplasm_attribute_value_external_references
UPDATE public.germplasm_attribute_value_external_references
SET soft_deleted = NEW.soft_deleted
WHERE germplasm_attribute_value_entity_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with germplasm_donor foreign key
CREATE OR REPLACE FUNCTION sync_germplasm_donor_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update germplasm_donor_external_references
UPDATE public.germplasm_donor_external_references
SET soft_deleted = NEW.soft_deleted
WHERE donor_entity_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with pedigree_node foreign key
CREATE OR REPLACE FUNCTION sync_pedigree_node_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update pedigree_node_external_references
UPDATE public.pedigree_node_external_references
SET soft_deleted = NEW.soft_deleted
WHERE pedigree_node_entity_id = NEW.id;

-- Update pedigree_edge
UPDATE public.pedigree_edge
SET soft_deleted = NEW.soft_deleted
WHERE connected_node_id = NEW.id;

-- Update pedigree_edge
UPDATE public.pedigree_edge
SET soft_deleted = NEW.soft_deleted
WHERE this_node_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with pedigree_edge foreign key
CREATE OR REPLACE FUNCTION sync_pedigree_edge_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update pedigree_edge_external_references
UPDATE public.pedigree_edge_external_references
SET soft_deleted = NEW.soft_deleted
WHERE pedigree_edge_entity_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with observation_unit foreign key
CREATE OR REPLACE FUNCTION sync_observation_unit_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update observation_unit_external_references
UPDATE public.observation_unit_external_references
SET soft_deleted = NEW.soft_deleted
WHERE observation_unit_entity_id = NEW.id;

-- Update observation
UPDATE public.observation
SET soft_deleted = NEW.soft_deleted
WHERE observation_unit_id = NEW.id;

-- Update observation_unit_level
UPDATE public.observation_unit_level
SET soft_deleted = NEW.soft_deleted
WHERE observation_unit_id = NEW.id;

-- Update observation_unit_position
UPDATE public.observation_unit_position
SET soft_deleted = NEW.soft_deleted
WHERE observation_unit_id = NEW.id;

-- Update observation_unit_treatment
UPDATE public.observation_unit_treatment
SET soft_deleted = NEW.soft_deleted
WHERE observation_unit_id = NEW.id;

-- Update event_observation_units
UPDATE public.event_observation_units
SET soft_deleted = NEW.soft_deleted
WHERE observation_units_id = NEW.id;

-- Update image
UPDATE public.image
SET soft_deleted = NEW.soft_deleted
WHERE observation_unit_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with image foreign key
CREATE OR REPLACE FUNCTION sync_image_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update image_entity_descriptive_ontology_terms
UPDATE public.image_entity_descriptive_ontology_terms
SET soft_deleted = NEW.soft_deleted
WHERE image_entity_id = NEW.id;

-- Update image_external_references
UPDATE public.image_external_references
SET soft_deleted = NEW.soft_deleted
WHERE image_entity_id = NEW.id;

-- Update image_observations
UPDATE public.image_observations
SET soft_deleted = NEW.soft_deleted
WHERE image_entity_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with reference_set foreign key
CREATE OR REPLACE FUNCTION sync_reference_set_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update reference_set_external_references
UPDATE public.reference_set_external_references
SET soft_deleted = NEW.soft_deleted
WHERE reference_set_entity_id = NEW.id;

-- Update reference
UPDATE public.reference
SET soft_deleted = NEW.soft_deleted
WHERE reference_set_id = NEW.id;

-- Update variant
UPDATE public.variant
SET soft_deleted = NEW.soft_deleted
WHERE reference_set_id = NEW.id;

-- Update variantset
UPDATE public.variantset
SET soft_deleted = NEW.soft_deleted
WHERE reference_set_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with reference foreign key
CREATE OR REPLACE FUNCTION sync_reference_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update reference_bases
UPDATE public.reference_bases
SET soft_deleted = NEW.soft_deleted
WHERE reference_id = NEW.id;

-- Update reference_external_references
UPDATE public.reference_external_references
SET soft_deleted = NEW.soft_deleted
WHERE reference_entity_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with reference_bases foreign key
CREATE OR REPLACE FUNCTION sync_reference_bases_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update reference_bases_external_references
UPDATE public.reference_bases_external_references
SET soft_deleted = NEW.soft_deleted
WHERE reference_bases_page_entity_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with variant foreign key
CREATE OR REPLACE FUNCTION sync_variant_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update variant_external_references
UPDATE public.variant_external_references
SET soft_deleted = NEW.soft_deleted
WHERE variant_entity_id = NEW.id;

-- Update variant_entity_alternate_bases
UPDATE public.variant_entity_alternate_bases
SET soft_deleted = NEW.soft_deleted
WHERE variant_entity_id = NEW.id;

-- Update variant_entity_ciend
UPDATE public.variant_entity_ciend
SET soft_deleted = NEW.soft_deleted
WHERE variant_entity_id = NEW.id;

-- Update variant_entity_cipos
UPDATE public.variant_entity_cipos
SET soft_deleted = NEW.soft_deleted
WHERE variant_entity_id = NEW.id;

-- Update variant_entity_filters_failed
UPDATE public.variant_entity_filters_failes
SET soft_deleted = NEW.soft_deleted
WHERE variant_entity_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger function to update soft_deleted status for records with variantset foreign key
CREATE OR REPLACE FUNCTION sync_variantset_related_tables_soft_deleted()
RETURNS TRIGGER AS $$
BEGIN
-- Update variantset_analysis
UPDATE public.variantset_analysis
SET soft_deleted = NEW.soft_deleted
WHERE variant_set_id = NEW.id;

-- Update variantset_external_references
UPDATE public.variantset_external_references
SET soft_deleted = NEW.soft_deleted
WHERE variant_set_entity_id = NEW.id;

-- Update variantset_format
UPDATE public.variantset_format
SET soft_deleted = NEW.soft_deleted
WHERE variant_set_id = NEW.id;

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Create a trigger on the germplasm table
CREATE TRIGGER sync_germplasm_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.germplasm
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_germplasm_related_tables_soft_deleted();

-- Create a trigger on the germplasm_attribute_value table
CREATE TRIGGER sync_germplasm_attribute_value_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.germplasm_attribute_value
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_germplasm_attribute_value_related_tables_soft_deleted();

-- Create a trigger on the germplasm_donor table
CREATE TRIGGER sync_germplasm_donor_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.germplasm_donor
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_germplasm_donor_related_tables_soft_deleted();

-- Create a trigger on the pedigree_node table
CREATE TRIGGER sync_pedigree_node_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.pedigree_node
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_pedigree_node_related_tables_soft_deleted();

-- Create a trigger on the pedigree_edge table
CREATE TRIGGER sync_pedigree_edge_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.pedigree_edge
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_pedigree_edge_related_tables_soft_deleted();

-- Create a trigger on the observation_unit table
CREATE TRIGGER sync_observation_unit_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.observation_unit
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_observation_unit_related_tables_soft_deleted();

-- Create a trigger on the image table
CREATE TRIGGER sync_image_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.image
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_image_related_tables_soft_deleted();

-- Create a trigger on the reference_set table
CREATE TRIGGER sync_reference_set_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.reference_set
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_reference_set_related_tables_soft_deleted();

-- Create a trigger on the reference table
CREATE TRIGGER sync_reference_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.reference
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_reference_related_tables_soft_deleted();

-- Create a trigger on the reference table
CREATE TRIGGER sync_reference_bases_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.reference_bases
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_reference_bases_related_tables_soft_deleted();

-- Create a trigger on the variant table
CREATE TRIGGER sync_variant_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.variant
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_variant_related_tables_soft_deleted();

-- Create a trigger on the variantset table
CREATE TRIGGER sync_variantset_soft_deleted_status
    AFTER UPDATE OF soft_deleted ON public.variantset
    FOR EACH ROW
    WHEN (OLD.soft_deleted IS DISTINCT FROM NEW.soft_deleted)
EXECUTE FUNCTION sync_variantset_related_tables_soft_deleted();