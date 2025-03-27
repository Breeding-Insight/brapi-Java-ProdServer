DELETE FROM seed_lot_transaction_external_references where seed_lot_transaction_entity_id = 'seed_lot_transaction2';
DELETE FROM external_reference where id = 'seed_lot_transaction_er_2';
DELETE FROM seed_lot_transaction where id = 'seed_lot_transaction2';

DELETE FROM seed_lot_transaction_external_references where seed_lot_transaction_entity_id = 'seed_lot_transaction1';
DELETE FROM external_reference where id = 'seed_lot_transaction_er_1';
DELETE FROM seed_lot_transaction where id = 'seed_lot_transaction1';

DELETE FROM seed_lot_external_references where seed_lot_entity_id = 'seed_lot2';
DELETE FROM external_reference where id = 'seed_lot_er_2';
DELETE FROM seed_lot_content_mixture where seed_lot_id = 'seed_lot2';
DELETE FROM seed_lot where id = 'seed_lot2';

DELETE FROM seed_lot_external_references where seed_lot_entity_id = 'seed_lot1';
DELETE FROM external_reference where id = 'seed_lot_er_1';
DELETE FROM seed_lot_content_mixture where seed_lot_id = 'seed_lot1';
DELETE FROM seed_lot where id = 'seed_lot1';