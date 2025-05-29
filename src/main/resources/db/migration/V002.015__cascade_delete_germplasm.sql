-- This migration will delete records associated with a deleted germplasm

-- First, drop the existing foreign key constraint
ALTER TABLE ONLY public.germplasm_external_references
DROP CONSTRAINT IF EXISTS fkibd3k0wfmkkjf2budoeothsub;

ALTER TABLE ONLY public.germplasm_attribute_value
DROP CONSTRAINT IF EXISTS fkqqry5v3us53yf0o4dlcd7dxg8;

ALTER TABLE ONLY public.germplasm_donor
DROP CONSTRAINT IF EXISTS fk6e02we60r9ut32qqhxb86ca72;

ALTER TABLE ONLY public.germplasm_entity_type_of_germplasm_storage_code
DROP CONSTRAINT IF EXISTS fktahnvan5u79myt3kbqkijra78;

ALTER TABLE ONLY public.germplasm_institute
DROP CONSTRAINT IF EXISTS fkk9yp3d2siqb14sw6gvvrcspo6;

ALTER TABLE ONLY public.germplasm_origin
DROP CONSTRAINT IF EXISTS fk1m54g844qt69f241brkregk05;

ALTER TABLE ONLY public.germplasm_search_results
DROP CONSTRAINT IF EXISTS fk6ett5g30uvf3d4101e7ndoj8c;

ALTER TABLE ONLY public.germplasm_synonym
DROP CONSTRAINT IF EXISTS fkkm9j9i32axrnrs34qr9q6a63n;

ALTER TABLE ONLY public.germplasm_taxon
DROP CONSTRAINT IF EXISTS fkokfpuhyosimp5gdloucr73vja;

ALTER TABLE ONLY public.pedigree_node
DROP CONSTRAINT IF EXISTS fkkll27h6pngksanbpu2dnnldbt;

ALTER TABLE ONLY public.cross_parent
DROP CONSTRAINT IF EXISTS fkgwne8ni7iqgxeu5qvnjskbkvj;

ALTER TABLE ONLY public.observation_unit
DROP CONSTRAINT IF EXISTS fkgykwn3x9wx8m77wy67juqlxab;

ALTER TABLE ONLY public.reference_set
DROP CONSTRAINT IF EXISTS fko1ndd24soyk8191bqhg7fegdg;

ALTER TABLE ONLY public.seed_lot_content_mixture
DROP CONSTRAINT IF EXISTS fkc1gnrmj6ceuu1o4s6ffs4yjt3;

ALTER TABLE ONLY public.pedigree_edge
DROP CONSTRAINT IF EXISTS fk89pdbyb8xx3eua9b9i5cu5ltq;

ALTER TABLE ONLY public.pedigree_edge
DROP CONSTRAINT IF EXISTS fk5ds4uo4f3tsurnjtc8dxb7w6r;

ALTER TABLE ONLY public.pedigree_edge_external_references
DROP CONSTRAINT IF EXISTS fk8b66j4ff2axcsuosr5erotn8e;

ALTER TABLE ONLY public.observation_unit_external_references
DROP CONSTRAINT IF EXISTS fkel9v1egnbmp20dpbkt1831ljq;

ALTER TABLE ONLY public.observation
DROP CONSTRAINT IF EXISTS fkfqw685lfh2uxxbgi60vne24jr;

ALTER TABLE ONLY public.observation_unit_level
DROP CONSTRAINT IF EXISTS fk8iqhtkmddjbc4vhhf26ayx3b4;

ALTER TABLE ONLY public.observation_unit_position
DROP CONSTRAINT IF EXISTS fk2mpgvaq1ppb8kjp3jk0ecpdyr;

ALTER TABLE ONLY public.observation_unit_treatment
DROP CONSTRAINT IF EXISTS fk9aok9thr8d6dgqveur46e5985;

ALTER TABLE ONLY public.event_observation_units
DROP CONSTRAINT IF EXISTS fk3ecq9fb2rd3qampc61hunr7sw;

ALTER TABLE ONLY public.image
DROP CONSTRAINT IF EXISTS fkob3t0l8jr07byg0p3vd06yig4;

ALTER TABLE ONLY public.image_entity_descriptive_ontology_terms
DROP CONSTRAINT IF EXISTS fkce2lade5kp0pnd7dkps05s494;

ALTER TABLE ONLY public.image_external_references
DROP CONSTRAINT IF EXISTS fkt3uy354jynflg06911dxjlu1i;

ALTER TABLE ONLY public.image_observations
DROP CONSTRAINT IF EXISTS fkbwiawjcj7mihofl5puuq7k2ws;

ALTER TABLE ONLY public.reference_set_external_references
DROP CONSTRAINT IF EXISTS fkbk2b1k68f0drytr6pkncudxoh;

ALTER TABLE ONLY public.reference
DROP CONSTRAINT IF EXISTS fkpr6hqcaxgwgl0uom9wo22om65;

ALTER TABLE ONLY public.variant
DROP CONSTRAINT IF EXISTS fkh523hkn6t8iurf19bb19o7px7;

ALTER TABLE ONLY public.variantset
DROP CONSTRAINT IF EXISTS fkoy17h5s7f83vl8rfcpeqduhp3;

ALTER TABLE ONLY public.reference_bases
DROP CONSTRAINT IF EXISTS fkro4r722mjgl8huj88soea80le;

ALTER TABLE ONLY public.reference_external_references
DROP CONSTRAINT IF EXISTS fkr0nxjwbpehkbp8vfg3xyql5vy;

ALTER TABLE ONLY public.reference_bases_external_references
DROP CONSTRAINT IF EXISTS fkf6sny2j9k3hugyrp6iut22oo;

ALTER TABLE ONLY public.variant_external_references
DROP CONSTRAINT IF EXISTS fkgoibfri9bvwfh4jd8snqwcw5u;

ALTER TABLE ONLY public.variant_entity_alternate_bases
DROP CONSTRAINT IF EXISTS fkv6ptn907aedytbh5w8mc6t3r;

ALTER TABLE ONLY public.variant_entity_ciend
DROP CONSTRAINT IF EXISTS fk9dsm85o8qltahmd03symbgohp;

ALTER TABLE ONLY public.variant_entity_cipos
DROP CONSTRAINT IF EXISTS fkh94uec6mbay6eld1uxk1m2xbi;

ALTER TABLE ONLY public.variant_entity_filters_failed
DROP CONSTRAINT IF EXISTS fki10kljn84l12s881v3bxdh34n;

ALTER TABLE ONLY public.variantset_analysis
DROP CONSTRAINT IF EXISTS fko9jtegpt029eaca8mifd8yrm4;

ALTER TABLE ONLY public.variantset_external_references
DROP CONSTRAINT IF EXISTS fkckhrweboc7ktl0e7obei7731f;

ALTER TABLE ONLY public.variantset_format
DROP CONSTRAINT IF EXISTS fkse2qx2lkfhnb00nnoexcpa1fu;

-- Then, recreate the constraint with ON DELETE CASCADE
ALTER TABLE ONLY public.variantset_format
    ADD CONSTRAINT fkse2qx2lkfhnb00nnoexcpa1fu
    FOREIGN KEY (variant_set_id)
    REFERENCES public.variantset(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variantset_external_references
    ADD CONSTRAINT fkckhrweboc7ktl0e7obei7731f
    FOREIGN KEY (variant_set_entity_id)
    REFERENCES public.variantset(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variantset_analysis
    ADD CONSTRAINT fko9jtegpt029eaca8mifd8yrm4
    FOREIGN KEY (variant_set_id)
    REFERENCES public.variantset(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variant_entity_filters_failed
    ADD CONSTRAINT fki10kljn84l12s881v3bxdh34n
    FOREIGN KEY (variant_entity_id)
    REFERENCES public.variant(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variant_entity_cipos
    ADD CONSTRAINT fkh94uec6mbay6eld1uxk1m2xbi
    FOREIGN KEY (variant_entity_id)
    REFERENCES public.variant(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variant_entity_ciend
    ADD CONSTRAINT fk9dsm85o8qltahmd03symbgohp
    FOREIGN KEY (variant_entity_id)
    REFERENCES public.variant(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variant_entity_alternate_bases
    ADD CONSTRAINT fkv6ptn907aedytbh5w8mc6t3r
    FOREIGN KEY (variant_entity_id)
    REFERENCES public.variant(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variant_external_references
    ADD CONSTRAINT fkgoibfri9bvwfh4jd8snqwcw5u
    FOREIGN KEY (variant_entity_id)
    REFERENCES public.variant(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.reference_bases_external_references
    ADD CONSTRAINT fkf6sny2j9k3hugyrp6iut22oo
    FOREIGN KEY (reference_bases_page_entity_id)
    REFERENCES public.reference_bases(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.reference_external_references
    ADD CONSTRAINT fkr0nxjwbpehkbp8vfg3xyql5vy
    FOREIGN KEY (reference_entity_id)
    REFERENCES public.reference(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.reference_bases
    ADD CONSTRAINT fkro4r722mjgl8huj88soea80le
    FOREIGN KEY (reference_id)
    REFERENCES public.reference(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variantset
    ADD CONSTRAINT fkoy17h5s7f83vl8rfcpeqduhp3
    FOREIGN KEY (reference_set_id)
    REFERENCES public.reference_set(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.variant
    ADD CONSTRAINT fkh523hkn6t8iurf19bb19o7px7
    FOREIGN KEY (reference_set_id)
    REFERENCES public.reference_set(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.reference
    ADD CONSTRAINT fkpr6hqcaxgwgl0uom9wo22om65
    FOREIGN KEY (reference_set_id)
    REFERENCES public.reference_set(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.reference_set_external_references
    ADD CONSTRAINT fkbk2b1k68f0drytr6pkncudxoh
    FOREIGN KEY (reference_set_entity_id)
    REFERENCES public.reference_set(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.image_observations
    ADD CONSTRAINT fkbwiawjcj7mihofl5puuq7k2ws
    FOREIGN KEY (image_entity_id)
    REFERENCES public.image(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.image_external_references
    ADD CONSTRAINT fkt3uy354jynflg06911dxjlu1i
    FOREIGN KEY (image_entity_id)
    REFERENCES public.image(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.image_entity_descriptive_ontology_terms
    ADD CONSTRAINT fkce2lade5kp0pnd7dkps05s494
    FOREIGN KEY (image_entity_id)
    REFERENCES public.image(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.image
    ADD CONSTRAINT fkob3t0l8jr07byg0p3vd06yig4
    FOREIGN KEY (observation_unit_id)
    REFERENCES public.observation_unit(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.event_observation_units
    ADD CONSTRAINT fk3ecq9fb2rd3qampc61hunr7sw
    FOREIGN KEY (observation_units_id)
    REFERENCES public.observation_unit(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.observation_unit_treatment
    ADD CONSTRAINT fk9aok9thr8d6dgqveur46e5985
    FOREIGN KEY (observation_unit_id)
    REFERENCES public.observation_unit(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.observation_unit_position
    ADD CONSTRAINT fk2mpgvaq1ppb8kjp3jk0ecpdyr
    FOREIGN KEY (observation_unit_id)
    REFERENCES public.observation_unit(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.observation_unit_level
    ADD CONSTRAINT fk8iqhtkmddjbc4vhhf26ayx3b4
    FOREIGN KEY (observation_unit_id)
    REFERENCES public.observation_unit(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.observation
    ADD CONSTRAINT fkfqw685lfh2uxxbgi60vne24jr
    FOREIGN KEY (observation_unit_id)
    REFERENCES public.observation_unit(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.observation_unit_external_references
    ADD CONSTRAINT fkel9v1egnbmp20dpbkt1831ljq
    FOREIGN KEY (observation_unit_entity_id)
    REFERENCES public.observation_unit(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.pedigree_edge_external_references
    ADD CONSTRAINT fk8b66j4ff2axcsuosr5erotn8e
    FOREIGN KEY (pedigree_edge_entity_id)
    REFERENCES public.pedigree_edge(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.pedigree_edge
    ADD CONSTRAINT fk89pdbyb8xx3eua9b9i5cu5ltq
    FOREIGN KEY (this_node_id)
    REFERENCES public.pedigree_node(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.seed_lot_content_mixture
    ADD CONSTRAINT fkc1gnrmj6ceuu1o4s6ffs4yjt3
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.reference_set
    ADD CONSTRAINT fko1ndd24soyk8191bqhg7fegdg
    FOREIGN KEY (source_germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.observation_unit
    ADD CONSTRAINT fkgykwn3x9wx8m77wy67juqlxab
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.cross_parent
    ADD CONSTRAINT fkgwne8ni7iqgxeu5qvnjskbkvj
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.pedigree_node
    ADD CONSTRAINT fkkll27h6pngksanbpu2dnnldbt
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_taxon
    ADD CONSTRAINT fkokfpuhyosimp5gdloucr73vja
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_synonym
    ADD CONSTRAINT fkkm9j9i32axrnrs34qr9q6a63n
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_search_results
    ADD CONSTRAINT fk6ett5g30uvf3d4101e7ndoj8c
    FOREIGN KEY (germplasm_entity_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_origin
    ADD CONSTRAINT fk1m54g844qt69f241brkregk05
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_institute
    ADD CONSTRAINT fkk9yp3d2siqb14sw6gvvrcspo6
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_entity_type_of_germplasm_storage_code
    ADD CONSTRAINT fktahnvan5u79myt3kbqkijra78
    FOREIGN KEY (germplasm_entity_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_external_references
    ADD CONSTRAINT fkibd3k0wfmkkjf2budoeothsub
    FOREIGN KEY (germplasm_entity_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_attribute_value
    ADD CONSTRAINT fkqqry5v3us53yf0o4dlcd7dxg8
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;

ALTER TABLE ONLY public.germplasm_donor
    ADD CONSTRAINT fk6e02we60r9ut32qqhxb86ca72
    FOREIGN KEY (germplasm_id)
    REFERENCES public.germplasm(id)
    ON DELETE CASCADE;