package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CartItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Product product;
    private int quantity;
}
