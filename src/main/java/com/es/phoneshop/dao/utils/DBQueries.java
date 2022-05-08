package com.es.phoneshop.dao.utils;

public class DBQueries {
    public static final String FIND_ALL_PRODUCTS = "select p.id, code, description, p.price, p.currency, stock, image_url, " +
            "price_change_date, phe.price, phe.currency " +
            "from product as p " +
            "inner join price_history_entry as phe on p.id = phe.product_id " +
            "where stock > 0;";
    public static final String SAVE_PRODUCT = "insert into product (code, description, price, currency, stock, image_url) " +
            "values (?,?,?,?,?);";
    public static final String GET_LAST_INSERTED_PRODUCT_ID = "select max(id) from product;";
    public static final String UPDATE_PRICE_HISTORY = "insert into price_history_entry (product_id, price_change_date, " +
            "price, currency) values (?,?,?,?)";
    public static final String FIND_PRODUCT_BY_ID = FIND_ALL_PRODUCTS.replaceAll(".$", "") + " and p.id = ?";
    public static final String FIND_PRODUCT_PRICE_BY_ID = "select price, currency from product where id = ?;";
    public static final String UPDATE_PRODUCT = "update product " +
            "set code = ?, description = ?, price = ?, currency = ?, stock = ?, image_url = ? " +
            "where id = ?;";

    public static final String FIND_ALL_ORDERS = "select * from `order` where session_id = ?;";
    public static final String SAVE_ORDER = "insert into `order` (secure_id, subtotal_cost, delivery_cost, first_name, " +
            "last_name, phone, delivery_date, delivery_address, payment_method, session_id) " +
            "values (?,?,?,?,?,?,?,?,?,?);";
    public static final String FIND_ORDER_BY_ID = "select * " +
            "from `order` " +
            "where id = ?;";
    public static final String FIND_ORDER_BY_SECURE_ID = "select * " +
            "from `order` " +
            "where secure_id = ?;";

    public static final String FIND_PRODUCTS_BY_ORDER_ID = "select quantity, p.id, code, description, p.price, p.currency, " +
            "stock, image_url, price_change_date, phe.price, phe.currency " +
            "from order_item as o " +
            "inner join product as p on p.id = o.product_id " +
            "inner join price_history_entry as phe on p.id = phe.product_id " +
            "where stock > 0 " +
            "and o.order_id = ?;";
    public static final String GET_LAST_INSERTED_ORDER_ID = "select max(id) from `order`;";
}
