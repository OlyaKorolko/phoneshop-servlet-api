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
public class CartItem implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Product product;
    private int quantity;

    @Override
    public Object clone() throws CloneNotSupportedException {
        CartItem cartItem;
        try {
            cartItem = (CartItem) super.clone();
        } catch (CloneNotSupportedException e) {
            cartItem = new CartItem(this.product, this.quantity);
        }
        cartItem.product = (Product) this.product.clone();
        return cartItem;
    }
}
