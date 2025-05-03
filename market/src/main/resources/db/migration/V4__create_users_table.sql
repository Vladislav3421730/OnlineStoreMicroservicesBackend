
CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE public.users
(
    id           BIGINT PRIMARY KEY    DEFAULT nextval('user_id_seq'),
    username     VARCHAR(255) NOT NULL CHECK (LENGTH(username) >= 3),
    password     VARCHAR(255) NOT NULL CHECK (LENGTH(password) >= 6),
    email        VARCHAR(255) UNIQUE CHECK (POSITION('@' IN email) > 1),
    is_bun       BOOLEAN      NOT NULL DEFAULT FALSE,
    phone_number VARCHAR(13) UNIQUE CHECK (phone_number ~ '^[+]375[0-9]{9}$'),
    is_loyal     BOOLEAN DEFAULT FALSE
);

ALTER TABLE public.users OWNER TO postgres;

ALTER SEQUENCE public.user_id_seq OWNED BY public.users.id;

CREATE TABLE public.user_role
(
    user_id BIGINT      NOT NULL,
    role_set   VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE
);
