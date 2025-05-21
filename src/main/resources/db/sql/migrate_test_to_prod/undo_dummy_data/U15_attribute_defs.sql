DELETE FROM variable_base_entity_external_references WHERE variable_base_entity_id = 'attribute2';
DELETE FROM external_reference where id = 'attribute_er_2';
DELETE FROM variable_base_entity_synonyms where variable_base_entity_id = 'attribute2';
DELETE FROM variable_base_entity_context_of_use where variable_base_entity_id = 'attribute2';
DELETE FROM germplasm_attribute_definition where id = 'attribute2';
DELETE FROM trait where id = 'trait_attribute2';
DELETE FROM method where id = 'method_attribute2';
DELETE FROM scale where id = 'scale_attribute2';

DELETE FROM variable_base_entity_external_references WHERE variable_base_entity_id = 'attribute1';
DELETE FROM external_reference where id = 'attribute_er_1';
DELETE FROM variable_base_entity_synonyms where variable_base_entity_id = 'attribute1';
DELETE FROM variable_base_entity_context_of_use where variable_base_entity_id = 'attribute1';
DELETE FROM germplasm_attribute_definition where id = 'attribute1';
DELETE FROM trait where id = 'trait_attribute1';
DELETE FROM method where id = 'method_attribute1';
DELETE FROM scale where id = 'scale_attribute1';

DELETE FROM ontology where id = 'ontology_attribute1';

