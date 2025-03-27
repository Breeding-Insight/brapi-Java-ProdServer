DELETE FROM event_external_references where event_entity_id = 'event1';
DELETE FROM external_reference where id = 'event_er_1';

DELETE FROM event_param where event_id = 'event1';
DELETE FROM event_entity_dates where event_entity_id = 'event1';
DELETE FROM event where id = 'event1';
