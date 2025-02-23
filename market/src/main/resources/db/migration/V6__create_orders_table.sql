
CREATE SEQUENCE public.order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE public.orders
(
    id          BIGINT PRIMARY KEY      DEFAULT nextval('order_id_seq'),
    created_at  TIMESTAMP      NOT NULL DEFAULT now(),
    total_price NUMERIC(10, 2) NOT NULL CHECK (total_price >= 0.01),
    user_id     BIGINT         NOT NULL,
    address_id  BIGINT,
    status      VARCHAR(20)    NOT NULL DEFAULT 'ACCEPTED',
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE,
    CONSTRAINT fk_orders_address FOREIGN KEY (address_id) REFERENCES public.address (id) ON DELETE SET NULL
);

ALTER TABLE public.orders OWNER TO postgres;

ALTER SEQUENCE public.order_id_seq OWNED BY public.orders.id;