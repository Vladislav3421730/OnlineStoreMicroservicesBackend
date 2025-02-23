
CREATE SEQUENCE public.order_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE public.order_product
(
    id         BIGINT PRIMARY KEY DEFAULT nextval('order_item_id_seq'),
    product_id BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,
    amount     INT    NOT NULL CHECK (amount >= 1),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES public.product (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES public.orders (id) ON DELETE CASCADE
);



ALTER TABLE public.order_product OWNER TO postgres;

ALTER SEQUENCE public.order_item_id_seq OWNED BY public.order_product.id;
