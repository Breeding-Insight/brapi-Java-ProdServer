DELETE FROM sample_external_references where sample_entity_id = 'sample3';
DELETE FROM external_reference where id = 'sample_er_3';
DELETE FROM sample where id = 'sample3';

DELETE FROM sample_external_references where sample_entity_id = 'sample2';
DELETE FROM external_reference where id = 'sample_er_2';
DELETE FROM sample where id = 'sample2';

DELETE FROM sample_external_references where sample_entity_id = 'sample1';
DELETE FROM external_reference where id = 'sample_er_1';
DELETE FROM sample where id = 'sample1';

DELETE FROM plate where study_id = 'study1';
