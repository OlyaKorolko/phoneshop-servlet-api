package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public static final String CART_SESSION_ATTRIBUTE = "cart";
    private static volatile CartService instance;
    private final ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static synchronized CartService getInstance() {
        if (instance == null) {
            synchronized (DefaultCartService.class) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        readWriteLock.readLock().lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        readWriteLock.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, product.getStock(), quantity);
            }
            Optional<CartItem> foundCartItem = findCartItem(cart, product);
            if (foundCartItem.isPresent()) {
                foundCartItem.get().updateQuantity(quantity);
            } else {
                CartItem cartItem = new CartItem(product, quantity);
                product.setStock(product.getStock() - quantity);
                cart.getItems().add(cartItem);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
    private Optional<CartItem> findCartItem(Cart cart, Product product) {
        return cart.getItems()
                .stream()
                .filter(c -> c.getProduct().equals(product))
                .findFirst();
    }
}
