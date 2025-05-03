
CREATE SEQUENCE public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


CREATE TABLE public.product
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('product_id_seq'),
    title       VARCHAR(255)   NOT NULL CHECK (LENGTH(title) >= 3),
    description TEXT           NOT NULL CHECK (LENGTH(description) >= 10),
    category    VARCHAR(255)   NOT NULL CHECK (LENGTH(category) >= 3),
    amount      INTEGER        NOT NULL CHECK (amount >= 0),
    price       NUMERIC(10, 2) NOT NULL CHECK (price >= 0.01),
    priority    INTEGER NOT NULL CHECK (priority >=0 ) DEFAULT 0,
    discount    NUMERIC(10, 2) NOT NULL CHECK (discount >=0) DEFAULT 0.00
);
ALTER TABLE public.product OWNER TO postgres;

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;