DELETE FROM vendor_spec_status where id in ('vssta5', 'vssta4', 'vssta3', 'vssta2', 'vssta1');
DELETE FROM vendor_spec_deliverable where id = 'vsd1';
DELETE FROM vendor_spec_platform where id = 'vendor_spec_platform1';

DELETE FROM vendor_spec_well_position where id in ('vswp3', 'vswp2', 'vswp1');
DELETE FROM vendor_spec_sample_type where id in ('vssam3', 'vssam2', 'vssam1');
DELETE FROM vendor_spec_input_format where id in ('vsf1', 'vsf2');
DELETE FROM vendor_spec_requirement where id = 'vendor_spec_requirement1';

DELETE FROM vendor_spec where id = 'vendor_spec1';

DELETE FROM vendor_order_entity_service_ids where vendor_order_entity_id = 'vendor_order1';
UPDATE plate set submission_id = null where id = 'plate1';
DELETE FROM plate_submission where id = 'plate_submission1';
DELETE FROM vendor_order where id = 'vendor_order1';