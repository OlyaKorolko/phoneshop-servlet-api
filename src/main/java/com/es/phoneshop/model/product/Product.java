package com.es.phoneshop.model.product;

import com.es.phoneshop.model.BaseEntity;
import com.es.phoneshop.model.cart.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Getter
@Setter
public class Product extends BaseEntity implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private final String path = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer";
    private String imageUrl;
    private final List<PriceHistoryEntry> priceHistory;

    public Product() {
        priceHistory = new ArrayList<>();
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = path + imageUrl;
        priceHistory = new ArrayList<>();
        priceHistory.add(new PriceHistoryEntry(LocalDateTime.now(), price, currency));
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = path + imageUrl;
        priceHistory = new ArrayList<>();
        priceHistory.add(new PriceHistoryEntry(LocalDateTime.now(), price, currency));
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