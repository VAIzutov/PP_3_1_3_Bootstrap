INSERT INTO users (username, password, name, surname, age) VALUES
    ('izutov@bk.ru'
    , '$2a$10$N1UuCgWC3pNzh.W4zwWgb.WYk9pInHNC.a6vrYQuyZ8fW.B8HGsdO'
    , 'Vladimir'
    , 'Izutov'
    , '36');

INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO users_roles (users_id, roles_id) VALUES (1, 1)