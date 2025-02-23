CREATE SEQUENCE public.image_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.image
(
    id         BIGINT PRIMARY KEY DEFAULT nextval('image_id_seq'),
    name       VARCHAR(255) NOT NULL,
    type       VARCHAR(255) NOT NULL,
    path       VARCHAR(255) NOT NULL,
    product_id BIGINT,
    CONSTRAINT fk_image_product FOREIGN KEY (product_id) REFERENCES public.product (id) ON DELETE CASCADE
);

ALTER TABLE public.image OWNER TO postgres;

ALTER SEQUENCE public.image_id_seq OWNED BY public.image.id;
