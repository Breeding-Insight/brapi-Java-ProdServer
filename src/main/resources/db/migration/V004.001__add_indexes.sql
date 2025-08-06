-- Indexes for columns referenced by sync_list_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS list_external_references_list_id ON list_external_references (list_entity_id);
CREATE INDEX IF NOT EXISTS list_item_list_id ON list_item (list_id);

-- Indexes for columns referenced by sync_sample_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS sample_external_references_sample_entity_id ON sample_external_references (sample_entity_id);
CREATE INDEX IF NOT EXISTS vendor_file_sample_sample_dbid ON vendor_file_sample (sample_dbid);
CREATE INDEX IF NOT EXISTS callset_sample_id ON callset (sample_id);

-- Indexes for columns referenced by sync_trial_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS trial_external_references_trial_entity_id ON trial_external_references (trial_entity_id);
CREATE INDEX IF NOT EXISTS observation_unit_trial_id ON observation_unit (trial_id);
CREATE INDEX IF NOT EXISTS trial_publication_trial_id ON trial_publication (trial_id);
CREATE INDEX IF NOT EXISTS sample_trial_id ON sample (trial_id);
CREATE INDEX IF NOT EXISTS study_trial_id ON study (trial_id);
CREATE INDEX IF NOT EXISTS observation_trial_id ON observation (trial_id);
CREATE INDEX IF NOT EXISTS trial_dataset_authorship_trial_id ON trial_dataset_authorship (trial_id);
CREATE INDEX IF NOT EXISTS trial_contact_trial_db_id ON trial_contact (trial_db_id);
CREATE INDEX IF NOT EXISTS plate_trial_id ON plate (trial_id);

-- Indexes for columns referenced by sync_germplasm_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS germplasm_external_references_germplasm_entity_id ON germplasm_external_references (germplasm_entity_id);
CREATE INDEX IF NOT EXISTS germplasm_attribute_value_germplasm_id ON germplasm_attribute_value (germplasm_id);
CREATE INDEX IF NOT EXISTS germplasm_donor_germplasm_id ON germplasm_donor (germplasm_id);
-- The {table}_{column} naming convention would result in a name that is too long: germplasm_entity_type_of_germplasm_storage_code_germplasm_entity_id.
CREATE INDEX IF NOT EXISTS germplasm_entity_id ON germplasm_entity_type_of_germplasm_storage_code (germplasm_entity_id);
CREATE INDEX IF NOT EXISTS germplasm_institute_germplasm_id ON germplasm_institute (germplasm_id);
CREATE INDEX IF NOT EXISTS germplasm_origin_germplasm_id ON germplasm_origin (germplasm_id);
CREATE INDEX IF NOT EXISTS germplasm_search_results_germplasm_entity_id ON germplasm_search_results (germplasm_entity_id);
CREATE INDEX IF NOT EXISTS germplasm_synonym_germplasm_id ON germplasm_synonym (germplasm_id);
CREATE INDEX IF NOT EXISTS germplasm_taxon_germplasm_id ON germplasm_taxon (germplasm_id);
CREATE INDEX IF NOT EXISTS pedigree_node_germplasm_id ON pedigree_node (germplasm_id);
CREATE INDEX IF NOT EXISTS cross_parent_germplasm_id ON cross_parent (germplasm_id);
CREATE INDEX IF NOT EXISTS observation_unit_germplasm_id ON observation_unit (germplasm_id);
CREATE INDEX IF NOT EXISTS reference_set_source_germplasm_id ON reference_set (source_germplasm_id);
CREATE INDEX IF NOT EXISTS seed_lot_content_mixture_germplasm_id ON seed_lot_content_mixture (germplasm_id);

-- Indexes for columns referenced by sync_germplasm_attribute_value_related_tables_soft_deleted.
-- The {table}_{column} naming convention would result in a name that is too long: germplasm_attribute_value_external_references_germplasm_attribute_value_entity_id.
CREATE INDEX IF NOT EXISTS germplasm_attribute_value_entity_id ON germplasm_attribute_value_external_references (germplasm_attribute_value_entity_id);

-- Indexes for columns referenced by sync_germplasm_donor_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS germplasm_donor_external_references_donor_entity_id ON germplasm_donor_external_references (donor_entity_id);

-- Indexes for columns referenced by sync_pedigree_node_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS pedigree_node_external_references_pedigree_node_entity_id ON pedigree_node_external_references (pedigree_node_entity_id);
CREATE INDEX IF NOT EXISTS pedigree_edge_connected_node_id ON pedigree_edge (connected_node_id);
CREATE INDEX IF NOT EXISTS pedigree_edge_this_node_id ON pedigree_edge (this_node_id);

-- Indexes for columns referenced by sync_pedigree_edge_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS pedigree_edge_external_references_pedigree_edge_entity_id ON pedigree_edge_external_references (pedigree_edge_entity_id);

-- Indexes for columns referenced by sync_observation_unit_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS observation_unit_external_references_observation_unit_entity_id ON observation_unit_external_references (observation_unit_entity_id);
CREATE INDEX IF NOT EXISTS observation_observation_unit_id ON observation (observation_unit_id);
CREATE INDEX IF NOT EXISTS observation_unit_level_observation_unit_id ON observation_unit_level (observation_unit_id);
CREATE INDEX IF NOT EXISTS observation_unit_position_observation_unit_id ON observation_unit_position (observation_unit_id);
CREATE INDEX IF NOT EXISTS observation_unit_treatment_observation_unit_id ON observation_unit_treatment (observation_unit_id);
CREATE INDEX IF NOT EXISTS event_observation_units_observation_units_id ON event_observation_units (observation_units_id);
CREATE INDEX IF NOT EXISTS image_observation_unit_id ON image (observation_unit_id);

-- Indexes for columns referenced by sync_image_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS image_entity_descriptive_ontology_terms_image_entity_id ON image_entity_descriptive_ontology_terms (image_entity_id);
CREATE INDEX IF NOT EXISTS image_external_references_image_entity_id ON image_external_references (image_entity_id);
CREATE INDEX IF NOT EXISTS image_observations_image_entity_id ON image_observations (image_entity_id);

-- Indexes for columns referenced by sync_reference_set_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS reference_set_external_references_reference_set_entity_id ON reference_set_external_references (reference_set_entity_id);
CREATE INDEX IF NOT EXISTS reference_reference_set_id ON reference (reference_set_id);
CREATE INDEX IF NOT EXISTS variant_reference_set_id ON variant (reference_set_id);
CREATE INDEX IF NOT EXISTS variantset_reference_set_id ON variantset (reference_set_id);

-- Indexes for columns referenced by sync_reference_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS reference_bases_reference_id ON reference_bases (reference_id);
CREATE INDEX IF NOT EXISTS reference_external_references_reference_entity_id ON reference_external_references (reference_entity_id);

-- Indexes for columns referenced by sync_reference_bases_related_tables_soft_deleted.
-- The {table}_{column} naming convention would result in a name that is too long: reference_bases_external_references_reference_bases_page_entity_id.
CREATE INDEX IF NOT EXISTS reference_bases_page_entity_id ON reference_bases_external_references (reference_bases_page_entity_id);

-- Indexes for columns referenced by sync_variant_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS variant_external_references_variant_entity_id ON variant_external_references (variant_entity_id);
CREATE INDEX IF NOT EXISTS variant_entity_alternate_bases_variant_entity_id ON variant_entity_alternate_bases (variant_entity_id);
CREATE INDEX IF NOT EXISTS variant_entity_ciend_variant_entity_id ON variant_entity_ciend (variant_entity_id);
CREATE INDEX IF NOT EXISTS variant_entity_cipos_variant_entity_id ON variant_entity_cipos (variant_entity_id);
CREATE INDEX IF NOT EXISTS variant_entity_filters_failed_variant_entity_id ON variant_entity_filters_failed (variant_entity_id);

-- Indexes for columns referenced by sync_variantset_related_tables_soft_deleted.
CREATE INDEX IF NOT EXISTS variantset_analysis_variant_set_id ON variantset_analysis (variant_set_id);
CREATE INDEX IF NOT EXISTS variantset_external_references_variant_set_entity_id ON variantset_external_references (variant_set_entity_id);
CREATE INDEX IF NOT EXISTS variantset_format_variant_set_id ON variantset_format (variant_set_id);
