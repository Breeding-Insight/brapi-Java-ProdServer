DELETE FROM variable_base_entity_external_references where variable_base_entity_id = 'variable2';
DELETE FROM external_reference where id = 'variable_er_2';
DELETE FROM study_variable where study_db_id = 'study2';
DELETE FROM variable_base_entity_synonyms where variable_base_entity_id = 'variable2';
DELETE FROM variable_base_entity_context_of_use where variable_base_entity_id = 'variable2';
DELETE FROM observation_variable where id = 'variable2';

DELETE FROM trait where id = 'trait_variable2';
DELETE FROM method where id = 'method_variable2';
DELETE FROM scale where id = 'scale_variable2';

DELETE FROM variable_base_entity_external_references where variable_base_entity_id = 'variable1';
DELETE FROM external_reference where id = 'variable_er_1';
DELETE FROM study_variable where study_db_id = 'study1';
DELETE FROM variable_base_entity_synonyms where variable_base_entity_id = 'variable1';
DELETE FROM variable_base_entity_context_of_use where variable_base_entity_id = 'variable1';
DELETE FROM observation_variable where id = 'variable1';

DELETE FROM trait where id = 'trait_variable1';
DELETE FROM method where id = 'method_variable1';
DELETE FROM scale where id = 'scale_variable1';

DELETE FROM ontology where id = 'ontology_variable1';