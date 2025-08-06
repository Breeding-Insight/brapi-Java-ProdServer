CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER TABLE study_growth_facility DROP CONSTRAINT study_growth_facility_pkey;
ALTER TABLE study_growth_facility ADD COLUMN id UUID NOT NULL DEFAULT uuid_generate_v4();
ALTER TABLE study_growth_facility ADD PRIMARY KEY (id);
ALTER TABLE study_growth_facility ALTER COLUMN study_id DROP NOT NULL;
ALTER TABLE study_growth_facility ALTER COLUMN study_id DROP DEFAULT;
ALTER TABLE study_growth_facility ALTER COLUMN id DROP DEFAULT;

ALTER TABLE study_experimental_design DROP CONSTRAINT study_experimental_design_pkey;
ALTER TABLE study_experimental_design ADD COLUMN id UUID NOT NULL DEFAULT uuid_generate_v4();
ALTER TABLE study_experimental_design ADD PRIMARY KEY (id);
ALTER TABLE study_experimental_design ALTER COLUMN study_id DROP NOT NULL;
ALTER TABLE study_experimental_design ALTER COLUMN study_id DROP DEFAULT;
ALTER TABLE study_experimental_design ALTER COLUMN id DROP DEFAULT;

ALTER TABLE study_last_update DROP CONSTRAINT study_last_update_pkey;
ALTER TABLE study_last_update ADD COLUMN id UUID NOT NULL DEFAULT uuid_generate_v4();
ALTER TABLE study_last_update ADD PRIMARY KEY (id);
ALTER TABLE study_last_update ALTER COLUMN study_id DROP NOT NULL;
ALTER TABLE study_last_update ALTER COLUMN study_id DROP DEFAULT;
ALTER TABLE study_last_update ALTER COLUMN id DROP DEFAULT;