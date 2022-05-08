alter table price_history_entry
    drop foreign key price_history_entry_ibfk_1;
alter table order_item
    drop foreign key order_item_ibfk_1;
alter table order_item
    drop foreign key order_item_ibfk_2;

drop table if exists product;
create table if not exists product
(
    id          bigint primary key auto_increment,
    code        varchar(40)   not null,
    description varchar(1000) not null,
    price       decimal       not null,
    currency    varchar(6)    not null,
    stock       integer       not null,
    image_url   varchar(1000) not null
);
drop table if exists `order`;
create table if not exists `order`
(
    id               bigint primary key auto_increment,
    secure_id        varchar(40)   not null,
    subtotal_cost    decimal       not null,
    delivery_cost    decimal       not null,
    first_name       varchar(40)   not null,
    last_name        varchar(40)   not null,
    phone            varchar(40)   not null,
    delivery_date    timestamp     not null,
    delivery_address varchar(1000) not null,
    payment_method   varchar(20)   not null,
    session_id       varchar(100)  not null
);

drop table if exists order_item;
create table if not exists order_item
(
    order_id   bigint not null,
    product_id bigint not null,
    quantity   int    not null,
    foreign key (order_id) references `order` (id),
    foreign key (product_id) references product (id)
);

drop table if exists price_history_entry;
create table if not exists price_history_entry
(
    entry_id          bigint primary key auto_increment,
    product_id        bigint,
    price_change_date timestamp  not null,
    price             decimal    not null,
    currency          varchar(6) not null,
    foreign key (product_id) references product (id)
);
