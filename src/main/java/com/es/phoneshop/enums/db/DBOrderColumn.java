package com.es.phoneshop.enums.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DBOrderColumn {
    ID_COLUMN("id"),
    SECURE_ID_COLUMN("secure_id"),
    SUBTOTAL_COST_COLUMN("subtotal_cost"),
    DELIVERY_COST_COLUMN("delivery_cost"),
    FIRST_NAME_COLUMN("first_name"),
    LAST_NAME_COLUMN("last_name"),
    PHONE_COLUMN("phone"),
    DELIVERY_DATE_COLUMN("delivery_date"),
    DELIVERY_ADDRESS_COLUMN("delivery_address"),
    PAYMENT_METHOD_COLUMN("payment_method");

    private final String value;
}
