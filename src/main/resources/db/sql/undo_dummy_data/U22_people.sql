DELETE FROM person_external_references where person_entity_id = 'person2';
DELETE FROM external_reference where id = 'person_er_2';
DELETE FROM person where id = 'person2';

DELETE FROM person_external_references where person_entity_id = 'person1';
DELETE FROM external_reference where id = 'person_er_1';
DELETE FROM person where id = 'person1';