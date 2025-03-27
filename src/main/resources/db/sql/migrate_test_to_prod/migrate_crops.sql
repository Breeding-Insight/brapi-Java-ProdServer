CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create replacement row, can alternatively pass your own generated uuid in hand so you don't have to fetch the created one for the next part.
INSERT INTO crop (id, auth_user_id, crop_name) values (uuid_generate_v4(), 'AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA', 'your_crop_name');
-- Now, with the id of the crop you need to replace on hand, iterate through all the foreign key tables, replacing:
update genome_map set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update location set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update program set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update observation_variable set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update germplasm_attribute_definition set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update crop_external_references set crop_entity_id = 'new_uuid' where crop_entity_id = 'old_non_uuid';
update trial set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update germplasm set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update observation set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update study set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update variable_base_entity set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';
update observation_unit set crop_id = 'new_uuid' where crop_id = 'old_non_uuid';










