DELETE FROM observation_unit_external_references where observation_unit_entity_id = 'observation_unit3';
DELETE FROM external_reference where id = 'observation_unit_er_3';
DELETE FROM observation_unit_level where position_id = 'observation_unit_position3';
DELETE FROM observation_unit_position where id = 'observation_unit_position3';
DELETE FROM coordinate where id = 'observation_unit_coor_3';
DELETE FROM geojson where id = 'observation_unit_geo_3';
DELETE FROM observation_unit_treatment where observation_unit_id = 'observation_unit3';
DELETE FROM observation_unit where id = 'observation_unit3';

DELETE FROM observation_unit_external_references where observation_unit_entity_id = 'observation_unit2';
DELETE FROM external_reference where id = 'observation_unit_er_2';
DELETE FROM observation_unit_level where position_id = 'observation_unit_position2';
DELETE FROM observation_unit_position where id = 'observation_unit_position2';
DELETE FROM coordinate where id = 'observation_unit_coor_2';
DELETE FROM geojson where id = 'observation_unit_geo_2';
DELETE FROM observation_unit_treatment where observation_unit_id = 'observation_unit2';
DELETE FROM observation_unit where id = 'observation_unit2';

DELETE FROM observation_unit_external_references where observation_unit_entity_id = 'observation_unit1';
DELETE FROM external_reference where id = 'observation_unit_er_1';
DELETE FROM observation_unit_level where position_id = 'observation_unit_position1';
DELETE FROM observation_unit_position where id = 'observation_unit_position1';
DELETE FROM coordinate where id = 'observation_unit_coor_1';
DELETE FROM geojson where id = 'observation_unit_geo_1';
DELETE FROM observation_unit_treatment where observation_unit_id = 'observation_unit1';
DELETE FROM observation_unit where id = 'observation_unit1';