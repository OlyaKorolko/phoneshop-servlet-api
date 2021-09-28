package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

public class PriceHistoryEntry implements Serializable {
    private final LocalDateTime priceChangeDate;
    private final BigDecimal price;
    private final Currency currency;

    public PriceHistoryEntry(LocalDateTime priceChangeDate, BigDecimal price, Currency currency) {
        this.priceChangeDate = priceChangeDate;
        this.price = price;
        this.currency = currency;
    }

    public LocalDateTime getPriceChangeDate() {
        return priceChangeDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceHistoryEntry priceHistoryEntry = (PriceHistoryEntry) o;
        return Objects.equals(priceChangeDate, priceHistoryEntry.priceChangeDate) &&
                Objects.equals(price, priceHistoryEntry.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priceChangeDate, price);
    }

    @Override
    public String toString() {
        return "PriceTag{" +
                "priceChangeDate=" + priceChangeDate +
                ", price=" + price +
                '}';
    }
}
