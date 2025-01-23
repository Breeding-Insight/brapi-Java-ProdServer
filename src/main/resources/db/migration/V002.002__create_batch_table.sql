-- This migration creates the batch table for processing the delete method on a collection of BrAPI entities.

--
-- Name: batch; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch (
                             id UUID NOT NULL,
                             additional_info jsonb,
                             auth_user_id UUID,
                             date_created timestamp without time zone,
                             date_modified timestamp without time zone,
                             description text,
                             batch_name text,
                             batch_owner_name text,
                             batch_source text,
                             batch_type integer,
                             batch_owner_person_id UUID
);


ALTER TABLE public.batch OWNER TO postgres;

--
-- Name: batch_external_references; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_external_references (
                                                 batch_entity_id UUID NOT NULL,
                                                 external_references_id UUID NOT NULL
);


ALTER TABLE public.batch_external_references OWNER TO postgres;

--
-- Name: batch_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_item (
                                  id UUID NOT NULL,
                                  item text,
                                  batch_id UUID
);


ALTER TABLE public.batch_item OWNER TO postgres;

