package com.es.phoneshop.enums.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DBProductColumn {
    ID_COLUMN("p.id"),
    CODE_COLUMN("code"),
    DESCRIPTION_COLUMN("description"),
    PRICE_COLUMN("p.price"),
    CURRENCY_COLUMN("p.currency"),
    STOCK_COLUMN("stock"),
    IMAGE_URL_COLUMN("image_url"),
    LAST_INSERTED_ITEM("max(id)"),
    QUANTITY_COLUMN("quantity");

    private final String value;
}
