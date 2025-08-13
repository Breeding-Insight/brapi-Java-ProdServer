ALTER TABLE study_growth_facility DROP CONSTRAINT study_growth_facility_pkey;
ALTER TABLE study_growth_facility DROP COLUMN id;
ALTER TABLE study_growth_facility ADD PRIMARY KEY (study_id);

ALTER TABLE study_experimental_design DROP CONSTRAINT study_experimental_design_pkey;
ALTER TABLE study_experimental_design DROP COLUMN id;
ALTER TABLE study_experimental_design ADD PRIMARY KEY (study_id);

ALTER TABLE study_last_update DROP CONSTRAINT study_last_update_pkey;
ALTER TABLE study_last_update DROP COLUMN id;
ALTER TABLE study_last_update ADD PRIMARY KEY (study_id);