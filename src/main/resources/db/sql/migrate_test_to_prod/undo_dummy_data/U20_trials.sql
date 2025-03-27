DELETE FROM trial_external_references where trial_entity_id = 'trial3';
DELETE FROM external_reference where id = 'trial_er_3';
DELETE FROM trial_publication where trial_id = 'trial3';
DELETE FROM trial_dataset_authorship where trial_id = 'trial3';
DELETE FROM trial_contact where trial_db_id = 'trial3';
DELETE FROM person where id = 'trial_contact_3';
DELETE FROM trial where id = 'trial3';

DELETE FROM trial_external_references where trial_entity_id = 'trial2';
DELETE FROM external_reference where id = 'trial_er_2';
DELETE FROM trial_publication where trial_id = 'trial2';
DELETE FROM trial_dataset_authorship where trial_id = 'trial2';
DELETE FROM trial_contact where trial_db_id = 'trial2';
DELETE FROM person where id = 'trial_contact_2';
DELETE FROM trial where id = 'trial2';

DELETE FROM trial_external_references where trial_entity_id = 'trial1';
DELETE FROM external_reference where id = 'trial_er_1';
DELETE FROM trial_publication where trial_id = 'trial1';
DELETE FROM trial_dataset_authorship where trial_id = 'trial1';
DELETE FROM trial_contact where trial_db_id = 'trial1';
DELETE FROM person where id = 'trial_contact_1';
DELETE FROM trial where id = 'trial1';