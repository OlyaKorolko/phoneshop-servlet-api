package com.es.phoneshop.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
public class ProductPrice {
    private BigDecimal price;
    private Currency currency;
}
