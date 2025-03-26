DELETE FROM germplasm_attribute_value_external_references where germplasm_attribute_value_entity_id = 'attribute_val3';
DELETE FROM external_reference where id = 'attribute_val_er_3';
DELETE FROM germplasm_attribute_value where id = 'attribute_val3';

DELETE FROM germplasm_attribute_value_external_references where germplasm_attribute_value_entity_id = 'attribute_val2';
DELETE FROM external_reference where id = 'attribute_val_er_2';
DELETE FROM germplasm_attribute_value where id = 'attribute_val2';

DELETE FROM germplasm_attribute_value_external_references where germplasm_attribute_value_entity_id = 'attribute_val1';
DELETE FROM external_reference where id = 'attribute_val_er_1';
DELETE FROM germplasm_attribute_value where id = 'attribute_val1';