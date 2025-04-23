alter table study_growth_facility drop constraint study_growth_facility_pkey;
alter table study_growth_facility drop column id;
alter table study_growth_facility add primary key (study_id);

alter table study_experimental_design drop constraint study_experimental_design_pkey;
alter table study_experimental_design drop column id;
alter table study_experimental_design add primary key (study_id);

alter table study_last_update drop constraint study_last_update_pkey;
alter table study_last_update drop column id;
alter table study_last_update add primary key (study_id);