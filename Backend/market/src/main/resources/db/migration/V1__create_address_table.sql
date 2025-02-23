
CREATE SEQUENCE public.address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE public.address
(
    id            BIGINT PRIMARY KEY DEFAULT nextval('address_id_seq'),
    region        VARCHAR(255) NOT NULL,
    town          VARCHAR(255) NOT NULL,
    exact_address VARCHAR(255) NOT NULL,
    postal_code   VARCHAR(255)
);

ALTER TABLE public.address OWNER TO postgres;

ALTER SEQUENCE public.address_id_seq OWNED BY public.address.id;






