package com.es.phoneshop.model.product;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Builder
@Data
public class PriceHistoryEntry implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final LocalDateTime priceChangeDate;
    private final BigDecimal price;
    private final Currency currency;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
