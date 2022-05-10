package com.es.phoneshop.model.product;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "priceHistory")
@Builder
public class Product implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String path = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer";
    private Long id;
    private String code;
    private String description;
    private BigDecimal price;
    private Currency currency;
    private int stock;
    private String imageUrl;
    private final List<PriceHistoryEntry> priceHistory = new ArrayList<>();

    public static ProductBuilder builder() {
        return new CustomProductBuilder();
    }

    private static class CustomProductBuilder extends ProductBuilder {
        @Override
        public ProductBuilder imageUrl(String imageUrl) {
            return super.imageUrl(path + imageUrl);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Product product;
        try {
            product = (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            product = new Product(this.id, this.code, this.description, this.price, this.currency, this.stock, this.imageUrl);
        }
        for (int i = 0; i < priceHistory.size(); i++) {
            product.priceHistory.set(i, (PriceHistoryEntry) this.priceHistory.get(i).clone());
        }
        return product;
    }
}