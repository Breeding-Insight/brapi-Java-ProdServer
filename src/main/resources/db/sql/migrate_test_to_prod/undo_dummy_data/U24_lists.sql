DELETE FROM list_external_references where external_references_id = 'list_er_1';
DELETE FROM external_reference where id = 'list_er_1';

DELETE FROM list_item where list_id = 'list2';
DELETE FROM list where id = 'list2';

DELETE FROM list_item where list_id = 'list1';
DELETE FROM list where id = 'list1';

DELETE FROM person where id = 'list_person_1';