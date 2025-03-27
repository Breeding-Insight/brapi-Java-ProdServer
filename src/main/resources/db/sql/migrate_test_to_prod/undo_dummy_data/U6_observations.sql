DELETE FROM observation_external_references where observation_entity_id = 'observation4';
DELETE FROM external_reference where id = 'observation_er_4';
DELETE FROM observation where id = 'observation4';
DELETE FROM coordinate where id = 'observation_coor_4';
DELETE FROM geojson where id = 'observation_geo_4';

DELETE FROM observation_external_references where observation_entity_id = 'observation3';
DELETE FROM external_reference where id = 'observation_er_3';
DELETE FROM observation where id = 'observation3';
DELETE FROM coordinate where id = 'observation_coor_3';
DELETE FROM geojson where id = 'observation_geo_3';

DELETE FROM observation_external_references where observation_entity_id = 'observation2';
DELETE FROM external_reference where id = 'observation_er_2';
DELETE FROM observation where id = 'observation2';
DELETE FROM coordinate where id = 'observation_coor_2';
DELETE FROM geojson where id = 'observation_geo_2';

DELETE FROM observation_external_references where observation_entity_id = 'observation1';
DELETE FROM external_reference where id = 'observation_er_1';
DELETE FROM observation where id = 'observation1';
DELETE FROM coordinate where id = 'observation_coor_1';
DELETE FROM geojson where id = 'observation_geo_1';