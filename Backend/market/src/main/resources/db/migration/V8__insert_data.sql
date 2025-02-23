INSERT INTO public.address (region, town, exact_address, postal_code)
VALUES
    ('Московская область', 'Москва', 'ул. Тверская, д. 1', '125009'),
    ('Санкт-Петербург', 'Санкт-Петербург', 'наб. реки Мойки, д. 12', '190000'),
    ('Рязанская область', 'Рязань', 'ул. Станкостроителей, д. 45', '390000'),
    ('Новосибирская область', 'Новосибирск', 'ул. Ленина, д. 58', '630000'),
    ('Ростовская область', 'Ростов-на-Дону', 'пр. Стачки, д. 97', '344000');

INSERT INTO public.product (title, description, category, amount, coast)
VALUES
    ('Ноутбук Lenovo', 'Мощный ноутбук с процессором Intel i7, 16GB RAM и SSD 512GB', 'Электроника', 15, 49999.99),
    ('Смартфон Samsung Galaxy', 'Смартфон с экраном 6.5 дюймов, 128GB памяти', 'Электроника', 25, 29999.50),
    ('Холодильник LG', 'Холодильник с морозильной камерой, объем 350 литров', 'Бытовая техника', 10, 23999.00),
    ('Телевизор Sony 55"', 'Телевизор с разрешением 4K, диагональ 55 дюймов', 'Электроника', 12, 49999.99),
    ('Пылесос Bosch', 'Пылесос с системой фильтрации, мощность 2200W', 'Бытовая техника', 20, 10999.99),
    ('Микроволновка Samsung', 'Микроволновая печь с функцией гриля и мощностью 1000W', 'Бытовая техника', 18, 7999.00);


INSERT INTO public.image (name, type, path, product_id)
VALUES
    ('image1_1.jpg', 'image/jpeg', 'image1_1.jpg', 1),
    ('image1_2.jpg', 'image/jpeg', 'image1_2.jpg', 1),
    ('image1_3.jpg', 'image/jpeg', 'image1_3.jpg', 1),

    ('image2_1.jpg', 'image/jpeg', 'image2_1.jpg', 2),
    ('image2_2.jpg', 'image/jpeg', 'image2_2.jpg', 2),
    ('image2_3.jpg', 'image/jpeg', 'image2_3.jpg', 2),

    ('image3_1.jpg', 'image/jpeg', 'image3_1.jpg', 3),
    ('image3_2.jpg', 'image/jpeg', 'image3_2.jpg', 3),
    ('image3_3.jpg', 'image/jpeg', 'image3_3.jpg', 3),

    ('image4_1.jpg', 'image/jpeg', 'image4_1.jpg', 4),
    ('image4_2.jpg', 'image/jpeg', 'image4_2.jpg', 4),
    ('image4_3.jpg', 'image/jpeg', 'image4_3.jpg', 4),

    ('image5_1.jpg', 'image/jpeg', 'image5_1.jpg', 5),
    ('image5_2.jpg', 'image/jpeg', 'image5_2.jpg', 5),
    ('image5-3.jpg', 'image/jpeg', 'image5_3.jpg', 5),

    ('image6_1.jpg', 'image/jpeg', 'image6_1.jpg', 6),
    ('image6_2.jpg', 'image/jpeg', 'image6_2.jpg', 6),
    ('image6_3.jpg', 'image/jpeg', 'image6_3.jpg', 6);


INSERT INTO public.users (username, password, email, is_bun, phone_number)
VALUES
    ('vlad', '$2a$10$.UIemkDzflbT36.u3zgnCelPF/DziV9.jDytspUs.7Ee1bBMjy/5y', 'vlad@gmail.com', FALSE, '+375291234567'),
    ('vlad', '$2a$10$.UIemkDzflbT36.u3zgnCelPF/DziV9.jDytspUs.7Ee1bBMjy/5y', 'user@gmail.com', FALSE, '+375291234568'),
    ('vlad', '$2a$10$.UIemkDzflbT36.u3zgnCelPF/DziV9.jDytspUs.7Ee1bBMjy/5y', 'manager@gmail.com', FALSE, '+375291234569'),
    ('vlad', '$2a$10$.UIemkDzflbT36.u3zgnCelPF/DziV9.jDytspUs.7Ee1bBMjy/5y', 'admin@gmail.com', FALSE, '+375291234570');

INSERT INTO public.user_role (user_id, role_set)
VALUES
    (1, 'ROLE_USER'),
    (1, 'ROLE_MANAGER'),
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER'),
    (3, 'ROLE_MANAGER'),
    (4, 'ROLE_ADMIN');

INSERT INTO public.cart_of_goods (amount, product_id, user_id)
VALUES
    (2, 1, 1),
    (1, 2, 1),
    (3, 3, 2),
    (2, 5, 2),
    (1, 6, 3),
    (4, 4, 3),
    (1, 2, 4),
    (3, 4, 4);

INSERT INTO public.orders (total_price, user_id, address_id, status)
VALUES
    (99999.50, 1, 1, 'ACCEPTED'),
    (25999.00, 2, 2, 'ACCEPTED'),
    (22999.99, 3, 3, 'DELIVERED'),
    (12999.00, 4, 4, 'ACCEPTED');

INSERT INTO public.order_product (product_id, order_id, amount)
VALUES
    (1, 1, 2),
    (3, 1, 1),
    (5, 1, 1),
    (6, 1, 1);

INSERT INTO public.order_product (product_id, order_id, amount)
VALUES
    (2, 2, 1),
    (4, 2, 1),
    (6, 2, 1);

INSERT INTO public.order_product (product_id, order_id, amount)
VALUES
    (1, 3, 1),
    (3, 3, 2),
    (4, 3, 1);

INSERT INTO public.order_product (product_id, order_id, amount)
VALUES
    (2, 4, 1),
    (4, 4, 1),
    (6, 4, 1);




