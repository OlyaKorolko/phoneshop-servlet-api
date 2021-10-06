package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return "CartItem{" + product.getCode() +
                ", quantity=" + quantity +
                '}';
    }

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
