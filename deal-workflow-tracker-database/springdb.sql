--
-- PostgreSQL database dump
--

\restrict 0wfwyZ5kjabSalN9Mg99s3KzbWRhaP0az1JaoZ9ZoiaFa33e4vkI0dM3b6PUKdV

-- Dumped from database version 18.6
-- Dumped by pg_dump version 18.6

-- Started on 2026-09-06 16:17:59

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 5126 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 233 (class 1259 OID 16607)
-- Name: audit_logs; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.audit_logs (
    id bigint NOT NULL,
    action character varying(255),
    changed_on timestamp(6) without time zone,
    entity_id bigint,
    entity_name character varying(255),
    ip_address character varying(255),
    new_value text,
    old_value text,
    user_agent character varying(255),
    changed_by bigint
);


ALTER TABLE public.audit_logs OWNER TO springuser;

--
-- TOC entry 232 (class 1259 OID 16606)
-- Name: audit_logs_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

CREATE SEQUENCE public.audit_logs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audit_logs_id_seq OWNER TO springuser;

--
-- TOC entry 5128 (class 0 OID 0)
-- Dependencies: 232
-- Name: audit_logs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: springuser
--

ALTER SEQUENCE public.audit_logs_id_seq OWNED BY public.audit_logs.id;


--
-- TOC entry 229 (class 1259 OID 16558)
-- Name: bank_entities; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.bank_entities (
    id bigint NOT NULL,
    deal_card_id bigint,
    entity_id bigint,
    entity_name character varying(255),
    entity_type character varying(255),
    country character varying(255),
    created_by bigint,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint,
    updated_on timestamp without time zone
);


ALTER TABLE public.bank_entities OWNER TO springuser;

--
-- TOC entry 228 (class 1259 OID 16557)
-- Name: bank_entities_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

CREATE SEQUENCE public.bank_entities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bank_entities_id_seq OWNER TO springuser;

--
-- TOC entry 5129 (class 0 OID 0)
-- Dependencies: 228
-- Name: bank_entities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: springuser
--

ALTER SEQUENCE public.bank_entities_id_seq OWNED BY public.bank_entities.id;


--
-- TOC entry 227 (class 1259 OID 16547)
-- Name: deal_card; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.deal_card (
    id bigint NOT NULL,
    deal_name character varying(255),
    deal_type character varying(255),
    status character varying(255),
    borrower_name character varying(255),
    amount double precision,
    created_by bigint,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint,
    updated_on timestamp without time zone,
    last_updated_by character varying(255),
    status_notes character varying(255)
);


ALTER TABLE public.deal_card OWNER TO springuser;

--
-- TOC entry 226 (class 1259 OID 16546)
-- Name: deal_card_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

CREATE SEQUENCE public.deal_card_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.deal_card_id_seq OWNER TO springuser;

--
-- TOC entry 5130 (class 0 OID 0)
-- Dependencies: 226
-- Name: deal_card_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: springuser
--

ALTER SEQUENCE public.deal_card_id_seq OWNED BY public.deal_card.id;


--
-- TOC entry 231 (class 1259 OID 16575)
-- Name: facilities; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.facilities (
    id bigint NOT NULL,
    facility_name character varying(255),
    facility_type character varying(255),
    limit_amount double precision,
    entity_id bigint NOT NULL,
    created_by bigint,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint,
    updated_on timestamp without time zone
);


ALTER TABLE public.facilities OWNER TO springuser;

--
-- TOC entry 230 (class 1259 OID 16574)
-- Name: facilities_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

CREATE SEQUENCE public.facilities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.facilities_id_seq OWNER TO springuser;

--
-- TOC entry 5131 (class 0 OID 0)
-- Dependencies: 230
-- Name: facilities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: springuser
--

ALTER SEQUENCE public.facilities_id_seq OWNED BY public.facilities.id;


--
-- TOC entry 225 (class 1259 OID 16512)
-- Name: outbox; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.outbox (
    id bigint NOT NULL,
    aggregate_type character varying(255),
    aggregate_id bigint,
    event_type character varying(255),
    payload text,
    status character varying(50) DEFAULT 'PENDING'::character varying,
    retry_count integer DEFAULT 0,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    processed_on timestamp without time zone,
    error_message text
);


ALTER TABLE public.outbox OWNER TO springuser;

--
-- TOC entry 224 (class 1259 OID 16511)
-- Name: outbox_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

CREATE SEQUENCE public.outbox_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.outbox_id_seq OWNER TO springuser;

--
-- TOC entry 5132 (class 0 OID 0)
-- Dependencies: 224
-- Name: outbox_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: springuser
--

ALTER SEQUENCE public.outbox_id_seq OWNED BY public.outbox.id;


--
-- TOC entry 220 (class 1259 OID 16408)
-- Name: roles; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    created_by bigint,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint,
    updated_on timestamp without time zone
);


ALTER TABLE public.roles OWNER TO springuser;

--
-- TOC entry 219 (class 1259 OID 16407)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_id_seq OWNER TO springuser;

--
-- TOC entry 5133 (class 0 OID 0)
-- Dependencies: 219
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: springuser
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- TOC entry 223 (class 1259 OID 16440)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16420)
-- Name: users; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    username character varying(50) NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    postal_code character varying(7),
    created_by bigint,
    created_on timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by bigint,
    updated_on timestamp without time zone
);


ALTER TABLE public.users OWNER TO springuser;

--
-- TOC entry 221 (class 1259 OID 16419)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO springuser;

--
-- TOC entry 5135 (class 0 OID 0)
-- Dependencies: 221
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: springuser
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 235 (class 1259 OID 16696)
-- Name: workflows; Type: TABLE; Schema: public; Owner: springuser
--

CREATE TABLE public.workflows (
    id bigint NOT NULL,
    deal_card_id bigint NOT NULL,
    step_name character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    from_state character varying(50),
    to_state character varying(50),
    comments character varying(1000),
    action_by bigint,
    action_date timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.workflows OWNER TO springuser;

--
-- TOC entry 234 (class 1259 OID 16695)
-- Name: workflows_id_seq; Type: SEQUENCE; Schema: public; Owner: springuser
--

ALTER TABLE public.workflows ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.workflows_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 4911 (class 2604 OID 16610)
-- Name: audit_logs id; Type: DEFAULT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.audit_logs ALTER COLUMN id SET DEFAULT nextval('public.audit_logs_id_seq'::regclass);


--
-- TOC entry 4907 (class 2604 OID 16561)
-- Name: bank_entities id; Type: DEFAULT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.bank_entities ALTER COLUMN id SET DEFAULT nextval('public.bank_entities_id_seq'::regclass);


--
-- TOC entry 4905 (class 2604 OID 16550)
-- Name: deal_card id; Type: DEFAULT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.deal_card ALTER COLUMN id SET DEFAULT nextval('public.deal_card_id_seq'::regclass);


--
-- TOC entry 4909 (class 2604 OID 16578)
-- Name: facilities id; Type: DEFAULT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.facilities ALTER COLUMN id SET DEFAULT nextval('public.facilities_id_seq'::regclass);


--
-- TOC entry 4901 (class 2604 OID 16515)
-- Name: outbox id; Type: DEFAULT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.outbox ALTER COLUMN id SET DEFAULT nextval('public.outbox_id_seq'::regclass);


--
-- TOC entry 4897 (class 2604 OID 16411)
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- TOC entry 4899 (class 2604 OID 16423)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 5118 (class 0 OID 16607)
-- Dependencies: 233
-- Data for Name: audit_logs; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.audit_logs (id, action, changed_on, entity_id, entity_name, ip_address, new_value, old_value, user_agent, changed_by) FROM stdin;
1	STATE_TRANSITION	2026-09-05 14:48:59.446997	1	DealCard	0:0:0:0:0:0:0:1	UNDERWRITING	DRAFT	PostmanRuntime/7.56.1	1
2	STATE_TRANSITION	2026-09-05 16:32:04.044065	5	DealCard	0:0:0:0:0:0:0:1	UNDERWRITING	DRAFT	PostmanRuntime/7.56.1	1
3	STATE_TRANSITION	2026-09-05 18:44:27.448088	6	DealCard	0:0:0:0:0:0:0:1	UNDERWRITING	DRAFT	PostmanRuntime/7.56.1	1
4	STATE_TRANSITION	2026-09-05 19:06:17.569544	7	DealCard	0:0:0:0:0:0:0:1	UNDERWRITING	DRAFT	PostmanRuntime/7.56.1	1
5	STATE_TRANSITION	2026-09-05 19:14:20.543273	7	DealCard	0:0:0:0:0:0:0:1	COMPLIANCE_CHECK	UNDERWRITING	PostmanRuntime/7.56.1	1
6	STATE_TRANSITION	2026-09-05 19:15:38.544113	7	DealCard	0:0:0:0:0:0:0:1	APPROVED	COMPLIANCE_CHECK	PostmanRuntime/7.56.1	1
\.


--
-- TOC entry 5114 (class 0 OID 16558)
-- Dependencies: 229
-- Data for Name: bank_entities; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.bank_entities (id, deal_card_id, entity_id, entity_name, entity_type, country, created_by, created_on, updated_by, updated_on) FROM stdin;
3	3	\N	Acme Holdings Corp	BORROWER	Canada	1	2026-09-05 15:09:59.018725	\N	\N
4	4	\N	Acme Holdings Corp	BORROWER	Canada	1	2026-09-05 15:13:17.057794	\N	\N
5	5	\N	Acme Holdings Corp	BORROWER	Canada	1	2026-09-05 15:28:34.699953	\N	\N
6	6	\N	Acme Holdings Corp	BORROWER	Canada	1	2026-09-05 17:05:46.109513	\N	\N
7	7	\N	Acme Holdings Corp	BORROWER	Canada	\N	2026-09-05 19:05:40.021981	\N	\N
\.


--
-- TOC entry 5112 (class 0 OID 16547)
-- Dependencies: 227
-- Data for Name: deal_card; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.deal_card (id, deal_name, deal_type, status, borrower_name, amount, created_by, created_on, updated_by, updated_on, last_updated_by, status_notes) FROM stdin;
3	Project Tebline Acquisition	CREDIT	DRAFT	Acme Holdings Corp	2770000	1	2026-09-05 15:09:59.018725	\N	\N	\N	\N
4	Project Tebline Acquisition	CREDIT	DRAFT	Acme Holdings Corp	2770000	1	2026-09-05 15:13:17.057794	\N	\N	\N	\N
5	Project Meebline Acquisition	CREDIT	UNDERWRITING	Acme Holdings Corp	2780000	1	2026-09-05 15:28:34.699953	\N	\N	admin_user	\N
6	Project Kleebline Acquisition	CREDIT	UNDERWRITING	Acme Holdings Corp	3730000	1	2026-09-05 17:05:46.109513	1	\N	admin_user	\N
7	Project Yleebline Acquisition	CREDIT	APPROVED	Acme Holdings Corp	3750000	1	2026-09-05 19:05:40.021981	1	\N	admin_user	Approved by admin
\.


--
-- TOC entry 5116 (class 0 OID 16575)
-- Dependencies: 231
-- Data for Name: facilities; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.facilities (id, facility_name, facility_type, limit_amount, entity_id, created_by, created_on, updated_by, updated_on) FROM stdin;
3	Revolving Credit Line	REVOLVER	2770000	3	1	2026-09-05 15:09:59.018725	\N	\N
4	Revolving Credit Line	REVOLVER	2770000	4	1	2026-09-05 15:13:17.057794	\N	\N
5	Revolving Credit Line	REVOLVER	2780000	5	1	2026-09-05 15:28:34.699953	\N	\N
6	Revolving Credit Line	REVOLVER	3730000	6	1	2026-09-05 17:05:46.109513	\N	\N
7	Revolving Credit Line	REVOLVER	3750000	7	\N	2026-09-05 19:05:40.021981	\N	\N
\.


--
-- TOC entry 5110 (class 0 OID 16512)
-- Dependencies: 225
-- Data for Name: outbox; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.outbox (id, aggregate_type, aggregate_id, event_type, payload, status, retry_count, created_on, processed_on, error_message) FROM stdin;
1	DealCard	1	DEAL_CREATED	{"dealId": 1, "amount": 15000000.00, "status": "IN_PROGRESS"}	PENDING	0	2026-09-03 12:29:27.012264	\N	\N
\.


--
-- TOC entry 5105 (class 0 OID 16408)
-- Dependencies: 220
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.roles (id, name, created_by, created_on, updated_by, updated_on) FROM stdin;
1	ROLE_ADMIN	\N	2026-09-03 12:29:27.012264	\N	\N
2	ROLE_ANALYST	\N	2026-09-03 12:29:27.012264	\N	\N
3	ROLE_VIEWER	\N	2026-09-03 12:29:27.012264	\N	\N
\.


--
-- TOC entry 5108 (class 0 OID 16440)
-- Dependencies: 223
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (user_id, role_id) FROM stdin;
1	1
1	2
2	2
3	2
\.


--
-- TOC entry 5107 (class 0 OID 16420)
-- Dependencies: 222
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.users (id, username, first_name, last_name, email, password, postal_code, created_by, created_on, updated_by, updated_on) FROM stdin;
2	analyst_user	Sarah	Connor	sconnor@example.com	$2a$10$e8p2U3sXb8V4aI3S1t2Y9.JqG4Y1zU1W2O3R4T5Y6U7I8O9P0Q1R2	M5V3L9	\N	2026-09-03 12:29:27.012264	\N	\N
3	test_user	Test	User	testuser@example.com	$2a$10$R9viawqbZs8ard1sQPa97uXsJwhBPQkadGuRSvyTsiGCXSC1zDRsu	12345	\N	2026-09-03 18:59:08.625828	\N	\N
1	admin_user	System	Admin	admin@example.com	$2a$10$ojTM4DJvgxNN4yk7Wtk3E.yIn154Ux7z.Ko7HalQil2GsVSr6PERm	K1A0B1	\N	2026-09-03 12:29:27.012264	\N	\N
\.


--
-- TOC entry 5120 (class 0 OID 16696)
-- Dependencies: 235
-- Data for Name: workflows; Type: TABLE DATA; Schema: public; Owner: springuser
--

COPY public.workflows (id, deal_card_id, step_name, status, from_state, to_state, comments, action_by, action_date) FROM stdin;
1	6	SubmitForUnderwriting	UNDERWRITING	DRAFT	UNDERWRITING	\N	1	2026-09-05 14:44:27.410773-04
2	7	SubmitForUnderwriting	UNDERWRITING	DRAFT	UNDERWRITING	\N	1	2026-09-05 15:06:17.569544-04
3	7	PassUnderwriting	COMPLIANCE_CHECK	UNDERWRITING	COMPLIANCE_CHECK	\N	1	2026-09-05 15:14:20.521384-04
4	7	Approve	APPROVED	COMPLIANCE_CHECK	APPROVED	Approved by admin	1	2026-09-05 15:15:38.544113-04
\.


--
-- TOC entry 5136 (class 0 OID 0)
-- Dependencies: 232
-- Name: audit_logs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.audit_logs_id_seq', 6, true);


--
-- TOC entry 5137 (class 0 OID 0)
-- Dependencies: 228
-- Name: bank_entities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.bank_entities_id_seq', 7, true);


--
-- TOC entry 5138 (class 0 OID 0)
-- Dependencies: 226
-- Name: deal_card_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.deal_card_id_seq', 7, true);


--
-- TOC entry 5139 (class 0 OID 0)
-- Dependencies: 230
-- Name: facilities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.facilities_id_seq', 7, true);


--
-- TOC entry 5140 (class 0 OID 0)
-- Dependencies: 224
-- Name: outbox_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.outbox_id_seq', 1, true);


--
-- TOC entry 5141 (class 0 OID 0)
-- Dependencies: 219
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.roles_id_seq', 3, true);


--
-- TOC entry 5142 (class 0 OID 0)
-- Dependencies: 221
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.users_id_seq', 3, true);


--
-- TOC entry 5143 (class 0 OID 0)
-- Dependencies: 234
-- Name: workflows_id_seq; Type: SEQUENCE SET; Schema: public; Owner: springuser
--

SELECT pg_catalog.setval('public.workflows_id_seq', 4, true);


--
-- TOC entry 4936 (class 2606 OID 16615)
-- Name: audit_logs audit_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT audit_logs_pkey PRIMARY KEY (id);


--
-- TOC entry 4930 (class 2606 OID 16568)
-- Name: bank_entities bank_entities_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.bank_entities
    ADD CONSTRAINT bank_entities_pkey PRIMARY KEY (id);


--
-- TOC entry 4928 (class 2606 OID 16556)
-- Name: deal_card deal_card_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.deal_card
    ADD CONSTRAINT deal_card_pkey PRIMARY KEY (id);


--
-- TOC entry 4934 (class 2606 OID 16585)
-- Name: facilities facilities_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.facilities
    ADD CONSTRAINT facilities_pkey PRIMARY KEY (id);


--
-- TOC entry 4926 (class 2606 OID 16523)
-- Name: outbox outbox_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.outbox
    ADD CONSTRAINT outbox_pkey PRIMARY KEY (id);


--
-- TOC entry 4914 (class 2606 OID 16418)
-- Name: roles roles_name_key; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_name_key UNIQUE (name);


--
-- TOC entry 4916 (class 2606 OID 16416)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 4932 (class 2606 OID 16744)
-- Name: bank_entities uq_bank_entities_deal_card; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.bank_entities
    ADD CONSTRAINT uq_bank_entities_deal_card UNIQUE (deal_card_id);


--
-- TOC entry 4924 (class 2606 OID 16446)
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 4918 (class 2606 OID 16439)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4920 (class 2606 OID 16435)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4922 (class 2606 OID 16437)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4940 (class 2606 OID 16708)
-- Name: workflows workflows_pkey; Type: CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.workflows
    ADD CONSTRAINT workflows_pkey PRIMARY KEY (id);


--
-- TOC entry 4937 (class 1259 OID 16715)
-- Name: idx_audit_logs_entity; Type: INDEX; Schema: public; Owner: springuser
--

CREATE INDEX idx_audit_logs_entity ON public.audit_logs USING btree (entity_name, entity_id);


--
-- TOC entry 4938 (class 1259 OID 16714)
-- Name: idx_workflows_deal_card_id; Type: INDEX; Schema: public; Owner: springuser
--

CREATE INDEX idx_workflows_deal_card_id ON public.workflows USING btree (deal_card_id);


--
-- TOC entry 4945 (class 2606 OID 16652)
-- Name: deal_card fk4pxgfv9ypk37ey60w31yalwd6; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.deal_card
    ADD CONSTRAINT fk4pxgfv9ypk37ey60w31yalwd6 FOREIGN KEY (updated_by) REFERENCES public.users(id);


--
-- TOC entry 4954 (class 2606 OID 16616)
-- Name: audit_logs fk7lp4ppuidyl34g5lxxq1dskiy; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT fk7lp4ppuidyl34g5lxxq1dskiy FOREIGN KEY (changed_by) REFERENCES public.users(id);


--
-- TOC entry 4955 (class 2606 OID 16716)
-- Name: audit_logs fk_audit_logs_user; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT fk_audit_logs_user FOREIGN KEY (changed_by) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- TOC entry 4947 (class 2606 OID 16601)
-- Name: bank_entities fk_bank_entity_deal; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.bank_entities
    ADD CONSTRAINT fk_bank_entity_deal FOREIGN KEY (deal_card_id) REFERENCES public.deal_card(id) ON DELETE CASCADE;


--
-- TOC entry 4951 (class 2606 OID 16586)
-- Name: facilities fk_facility_entity; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.facilities
    ADD CONSTRAINT fk_facility_entity FOREIGN KEY (entity_id) REFERENCES public.bank_entities(id) ON DELETE CASCADE;


--
-- TOC entry 4943 (class 2606 OID 16452)
-- Name: user_roles fk_user_roles_role; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;


--
-- TOC entry 4944 (class 2606 OID 16447)
-- Name: user_roles fk_user_roles_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- TOC entry 4956 (class 2606 OID 16709)
-- Name: workflows fk_workflows_deal_card; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.workflows
    ADD CONSTRAINT fk_workflows_deal_card FOREIGN KEY (deal_card_id) REFERENCES public.deal_card(id) ON DELETE CASCADE;


--
-- TOC entry 4946 (class 2606 OID 16647)
-- Name: deal_card fka5wpb4hbhl2ac90ynhnjxm16r; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.deal_card
    ADD CONSTRAINT fka5wpb4hbhl2ac90ynhnjxm16r FOREIGN KEY (created_by) REFERENCES public.users(id);


--
-- TOC entry 4948 (class 2606 OID 16626)
-- Name: bank_entities fkesriir4d4f9qg5nber7f4476w; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.bank_entities
    ADD CONSTRAINT fkesriir4d4f9qg5nber7f4476w FOREIGN KEY (updated_by) REFERENCES public.users(id);


--
-- TOC entry 4941 (class 2606 OID 16636)
-- Name: roles fkf0p4aw14esgr0ukams27qfl3m; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT fkf0p4aw14esgr0ukams27qfl3m FOREIGN KEY (updated_by) REFERENCES public.users(id);


--
-- TOC entry 4952 (class 2606 OID 16657)
-- Name: facilities fkjptojq3hke4impn3f21pf3yrq; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.facilities
    ADD CONSTRAINT fkjptojq3hke4impn3f21pf3yrq FOREIGN KEY (created_by) REFERENCES public.users(id);


--
-- TOC entry 4949 (class 2606 OID 16621)
-- Name: bank_entities fkjw0iq9ac8hrqatnamrhudwxaj; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.bank_entities
    ADD CONSTRAINT fkjw0iq9ac8hrqatnamrhudwxaj FOREIGN KEY (created_by) REFERENCES public.users(id);


--
-- TOC entry 4953 (class 2606 OID 16662)
-- Name: facilities fklf7ht2src7niwyqsvix85frx1; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.facilities
    ADD CONSTRAINT fklf7ht2src7niwyqsvix85frx1 FOREIGN KEY (updated_by) REFERENCES public.users(id);


--
-- TOC entry 4950 (class 2606 OID 16690)
-- Name: bank_entities fkpo2ov8ndc4un7ppcymni2bgcy; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.bank_entities
    ADD CONSTRAINT fkpo2ov8ndc4un7ppcymni2bgcy FOREIGN KEY (entity_id) REFERENCES public.bank_entities(id);


--
-- TOC entry 4942 (class 2606 OID 16631)
-- Name: roles fkq6ium4se7bjk3mfbj3qm1gvy; Type: FK CONSTRAINT; Schema: public; Owner: springuser
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT fkq6ium4se7bjk3mfbj3qm1gvy FOREIGN KEY (created_by) REFERENCES public.users(id);


--
-- TOC entry 5127 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pg_database_owner
--

GRANT ALL ON SCHEMA public TO springuser;


--
-- TOC entry 5134 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE user_roles; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.user_roles TO springuser;


--
-- TOC entry 2092 (class 826 OID 16403)
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON SEQUENCES TO springuser;


--
-- TOC entry 2091 (class 826 OID 16402)
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: postgres
--

ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA public GRANT ALL ON TABLES TO springuser;


-- Completed on 2026-09-06 16:17:59

--
-- PostgreSQL database dump complete
--

\unrestrict 0wfwyZ5kjabSalN9Mg99s3KzbWRhaP0az1JaoZ9ZoiaFa33e4vkI0dM3b6PUKdV

