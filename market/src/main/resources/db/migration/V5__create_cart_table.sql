
CREATE SEQUENCE public.cart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE public.cart_of_goods
(
    id         BIGINT PRIMARY KEY DEFAULT nextval('cart_id_seq'),
    amount     INT    NOT NULL CHECK (amount >= 1),
    product_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES public.product (id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE
);



ALTER TABLE public.cart_of_goods OWNER TO postgres;

ALTER SEQUENCE public.cart_id_seq OWNED BY public.cart_of_goods.id;
