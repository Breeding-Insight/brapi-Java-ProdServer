DELETE FROM image_external_references where image_entity_id = 'image2';
DELETE FROM external_reference where id = 'image_er_2';

DELETE FROM image_entity_descriptive_ontology_terms where image_entity_id = 'image2';
DELETE FROM image where id = 'image2';
DELETE FROM coordinate where id = 'image_coor_2';
DELETE FROM geojson where id = 'image_geo_2';

DELETE FROM image_external_references where image_entity_id = 'image1';
DELETE FROM external_reference where id = 'image_er_1';

DELETE FROM image_entity_descriptive_ontology_terms where image_entity_id = 'image1';
DELETE FROM image where id = 'image1';
DELETE FROM coordinate where id = 'image_coor_1';
DELETE FROM geojson where id = 'image_geo_1';