UPDATE location set parent_location_id = null where id = 'location_01';

DELETE FROM location_external_references where location_entity_id = 'location_03';
DELETE FROM external_reference where id = 'location_er_3';
DELETE FROM location where id = 'location_03';
DELETE FROM coordinate where id = 'location_coor_7';
DELETE FROM geojson where id = 'location_geo_3';

DELETE FROM location_external_references where location_entity_id = 'location_02';
DELETE FROM external_reference where id = 'location_er_2';
DELETE FROM location where id = 'location_02';
DELETE FROM coordinate where geojson_id = 'location_geo_2';
DELETE FROM geojson where id = 'location_geo_2';

DELETE FROM location_external_references where location_entity_id = 'location_01';
DELETE FROM external_reference where id = 'location_er_1';
DELETE FROM location where id = 'location_01';
DELETE FROM coordinate where geojson_id = 'location_geo_1';
DELETE FROM geojson where id = 'location_geo_1';
