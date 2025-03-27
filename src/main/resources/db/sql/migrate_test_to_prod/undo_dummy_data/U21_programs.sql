UPDATE location set program_id = null, crop_id = null where id = 'location_03';
DELETE FROM program_external_references where program_entity_id = 'program3';
DELETE FROM external_reference where id = 'program_er_3';
DELETE FROM program where id = 'program3';
DELETE FROM person where id = 'program_person_3';

UPDATE location set program_id = null, crop_id = null where id = 'location_02';
DELETE FROM program_external_references where program_entity_id = 'program2';
DELETE FROM external_reference where id = 'program_er_2';
DELETE FROM program where id = 'program2';
DELETE FROM person where id = 'program_person_2';

UPDATE location set program_id = null, crop_id = null where id = 'location_01';
DELETE FROM program_external_references where program_entity_id = 'program1';
DELETE FROM external_reference where id = 'program_er_1';
DELETE FROM program where id = 'program1';
DELETE FROM person where id = 'program_person_1';