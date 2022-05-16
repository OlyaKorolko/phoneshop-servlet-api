package com.es.phoneshop.enums.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DBPriceHistoryColumn {
    PRICE_CHANGE_DATE_COLUMN("price_change_date"),
    PRICE_COLUMN("phe.price"),
    CURRENCY_COLUMN("phe.currency");

    private final String value;
}
