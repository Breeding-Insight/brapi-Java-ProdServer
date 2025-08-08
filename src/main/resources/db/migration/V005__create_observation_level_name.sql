CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE observation_unit_level_name (
     id UUID DEFAULT uuid_generate_v4(),
     CONSTRAINT observation_unit_level_name_pk PRIMARY KEY(id),
     level_name VARCHAR(64) NOT NULL,
     level_order INTEGER,
     program_id UUID,
     CONSTRAINT observation_unit_level_name_program_fk FOREIGN KEY(program_id) REFERENCES program(id)
);

CREATE UNIQUE INDEX lvl_name_program_id_idx ON observation_unit_level_name (level_name, program_id);

-- Set legacy level names
INSERT INTO observation_unit_level_name (level_name, level_order)
    VALUES
        ('study', 0),
        ('field', 1),
        ('entry', 2),
        ('rep', 3),
        ('block', 4),
        ('sub-block', 5),
        ('plot', 6),
        ('sub-plot', 7),
        ('plant', 8),
        ('pot', 9),
        ('sample', 10);

ALTER TABLE observation_unit_level
    ADD COLUMN  level_name_new UUID,
    ADD CONSTRAINT observation_unit_level_name_fk FOREIGN KEY (level_name_new) REFERENCES observation_unit_level_name(id);

-- Now migrate legacy enum data to use newly create entries in observation_unit_level_name table.

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'study') subquery
WHERE
	observation_unit_level.level_name = 0;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'field') subquery
WHERE
	observation_unit_level.level_name = 1;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'entry') subquery
WHERE
	observation_unit_level.level_name = 2;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'rep') subquery
WHERE
	observation_unit_level.level_name = 3;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'block') subquery
WHERE
	observation_unit_level.level_name = 4;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sub-block') subquery
WHERE
	observation_unit_level.level_name = 5;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'plot') subquery
WHERE
	observation_unit_level.level_name = 6;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sub-plot') subquery
WHERE
	observation_unit_level.level_name = 7;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'plant') subquery
WHERE
	observation_unit_level.level_name = 8;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'pot') subquery
WHERE
	observation_unit_level.level_name = 9;

UPDATE
	observation_unit_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sample') subquery
WHERE
	observation_unit_level.level_name = 10;

ALTER TABLE observation_unit_position
    ADD COLUMN  level_name_new UUID,
    ADD CONSTRAINT observation_unit_position_level_name_fk FOREIGN KEY (level_name_new) REFERENCES observation_unit_level_name(id);

-- Now migrate legacy enum data to use newly create entries in observation_unit_level_name table.

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'study') subquery
WHERE
	observation_unit_position.level_name = 0;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'field') subquery
WHERE
	observation_unit_position.level_name = 1;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'entry') subquery
WHERE
	observation_unit_position.level_name = 2;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'rep') subquery
WHERE
	observation_unit_position.level_name = 3;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'block') subquery
WHERE
	observation_unit_position.level_name = 4;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sub-block') subquery
WHERE
	observation_unit_position.level_name = 5;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'plot') subquery
WHERE
	observation_unit_position.level_name = 6;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sub-plot') subquery
WHERE
	observation_unit_position.level_name = 7;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'plant') subquery
WHERE
	observation_unit_position.level_name = 8;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'pot') subquery
WHERE
	observation_unit_position.level_name = 9;

UPDATE
	observation_unit_position
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sample') subquery
WHERE
	observation_unit_position.level_name = 10;

ALTER TABLE study_observation_level
    ADD COLUMN  level_name_new UUID,
    ADD CONSTRAINT study_observation_level_name_fk FOREIGN KEY (level_name_new) REFERENCES observation_unit_level_name(id);

-- Now migrate legacy enum data to use newly create entries in observation_unit_level_name table.

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'study') subquery
WHERE
	study_observation_level.level_name = 'study';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'field') subquery
WHERE
	study_observation_level.level_name = 'field';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'entry') subquery
WHERE
	study_observation_level.level_name = 'entry';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'rep') subquery
WHERE
	study_observation_level.level_name = 'rep';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'block') subquery
WHERE
	study_observation_level.level_name = 'block';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sub-block') subquery
WHERE
	study_observation_level.level_name = 'sub-block';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'plot') subquery
WHERE
	study_observation_level.level_name = 'plot';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sub-plot') subquery
WHERE
	study_observation_level.level_name = 'sub-plot';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'plant') subquery
WHERE
	study_observation_level.level_name = 'plant';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'pot') subquery
WHERE
	study_observation_level.level_name = 'pot';

UPDATE
	study_observation_level
SET
	level_name_new = subquery.id
FROM
	(
	SELECT
		id
	FROM
		observation_unit_level_name ouln
	WHERE
		ouln.level_name = 'sample') subquery
WHERE
	study_observation_level.level_name = 'sample';

-- TODO: Uncomment once the app has been tested
--ALTER TABLE observation_unit_position DROP COLUMN level_name, DROP COLUMN level_order, RENAME COLUMN level_name_new level_name
--ALTER TABLE observation_unit_level DROP COLUMN level_name, DROP COLUMN level_order, RENAME COLUMN level_name_new level_name
--ALTER TABLE study_observation_level DROP COLUMN level_name, DROP COLUMN level_order, RENAME COLUMN level_name_new level_name