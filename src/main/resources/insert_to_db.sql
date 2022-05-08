insert into product (code, description, price, currency, stock, image_url)
values ('sgs', 'Samsung Galaxy S', 100, 'USD', 100, '/Samsung/Samsung%20Galaxy%20S.jpg'),
       ('sgs2', 'Samsung Galaxy S II', 200, 'USD', 0, '/Samsung/Samsung%20Galaxy%20S%20II.jpg'),
       ('sgs3', 'Samsung Galaxy S III', 300, 'USD', 5, '/Samsung/Samsung%20Galaxy%20S%20III.jpg'),
       ('iphone', 'Apple iPhone', 200, 'USD', 10, '/Apple/Apple%20iPhone.jpg'),
       ('iphone6', 'Apple iPhone 6', 1000, 'USD', 30, '/Apple/Apple%20iPhone%206.jpg'),
       ('htces4g', 'HTC EVO Shift 4G', 320, 'USD', 3, '/HTC/HTC%20EVO%20Shift%204G.jpg'),
       ('sec901', 'Sony Ericsson C901', 420, 'USD', 30, '/Sony/Sony%20Ericsson%20C901.jpg'),
       ('xperiaxz', 'Sony Xperia XZ', 120, 'USD', 100, '/Sony/Sony%20Xperia%20XZ.jpg'),
       ('nokia3310', 'Nokia 3310', 70, 'USD', 100, '/Nokia/Nokia%203310.jpg'),
       ('palmp', 'Palm Pixi', 170, 'USD', 30, '/Palm/Palm%20Pixi.jpg'),
       ('simc56', 'Siemens C56', 70, 'USD', 20, '/Siemens/Siemens%20C56.jpg'),
       ('simc61', 'Siemens C61', 80, 'USD', 30, '/Siemens/Siemens%20C61.jpg'),
       ('simsxg75', 'Siemens SXG75', 150, 'USD', 40, '/Siemens/Siemens%20SXG75.jpg');

insert into price_history_entry (product_id, price_change_date, price, currency)
values (1, now(), 100, 'USD'),
       (2, now(), 200, 'USD'),
       (3, now(), 300, 'USD'),
       (4, now(), 200, 'USD'),
       (5, now(), 1000, 'USD'),
       (6, now(), 320, 'USD'),
       (7, now(), 420, 'USD'),
       (8, now(), 120, 'USD'),
       (9, now(), 70, 'USD'),
       (10, now(), 170, 'USD'),
       (11, now(), 70, 'USD'),
       (12, now(), 80, 'USD'),
       (13, now(), 150, 'USD');

