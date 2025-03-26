DELETE FROM allele_call where call_set_id = 'callset13';
DELETE FROM allele_call where call_set_id = 'callset12';
DELETE FROM allele_call where call_set_id = 'callset11';
DELETE FROM allele_call where call_set_id = 'callset10';
DELETE FROM allele_call where call_set_id = 'callset09';
DELETE FROM allele_call where call_set_id = 'callset08';
DELETE FROM allele_call where call_set_id = 'callset07';
DELETE FROM allele_call where call_set_id = 'callset06';
DELETE FROM allele_call where call_set_id = 'callset05';
DELETE FROM allele_call where call_set_id = 'callset04';
DELETE FROM allele_call where call_set_id = 'callset03';
DELETE FROM allele_call where call_set_id = 'callset02';
DELETE FROM allele_call where call_set_id = 'callset01';

DELETE FROM variant where variant_set_id = 'variantset1';

DELETE FROM callset_variant_sets where variant_sets_id = 'variantset1';
DELETE FROM callset where sample_id in ('sample1', 'sample2', 'sample3');

DELETE FROM variantset_external_references where variant_set_entity_id = 'variantset1';
DELETE FROM external_reference where id = 'variantset_er_1';
DELETE FROM variantset_format where variant_set_id = 'variantset1';
DELETE FROM variant_set_analysis_entity_software where variant_set_analysis_entity_id = 'variantset_analysis1';
DELETE FROM variantset_analysis where id = 'variantset_analysis1';

DELETE FROM variantset where id = 'variantset1';