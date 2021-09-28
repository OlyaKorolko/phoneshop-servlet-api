package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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
            Optional<CartItem> foundCartItem = findCartItem(cart, product);
            int foundItemQuantityInCart = foundCartItem.map(CartItem::getQuantity).orElse(0);

            if (product.getStock() < quantity + foundItemQuantityInCart) {
                throw new OutOfStockException(product, product.getStock(), quantity + foundItemQuantityInCart);
            }
            if (foundCartItem.isPresent()) {
                foundCartItem.get().setQuantity(foundItemQuantityInCart + quantity);
            } else {
                CartItem cartItem = new CartItem(product, quantity);
                cart.getItems().add(cartItem);
            }
            recalculateCardTotalQuantity(cart);
            recalculateCartTotalCost(cart);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private Optional<CartItem> findCartItem(Cart cart, Product product) {
        return cart.getItems()
                .stream()
                .filter(c -> c.getProduct().getId().equals(product.getId()))
                .findFirst();
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        readWriteLock.writeLock().lock();
        try {
            Product product = productDao.getProduct(productId);
            Optional<CartItem> foundCartItem = findCartItem(cart, product);

            if (product.getStock() < quantity) {
                throw new OutOfStockException(product, product.getStock(), quantity);
            }
            if (foundCartItem.isPresent()) {
                foundCartItem.get().setQuantity(quantity);
            } else {
                CartItem cartItem = new CartItem(product, quantity);
                cart.getItems().add(cartItem);
            }
            recalculateCardTotalQuantity(cart);
            recalculateCartTotalCost(cart);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
        recalculateCardTotalQuantity(cart);
        recalculateCartTotalCost(cart);
    }

    private void recalculateCardTotalQuantity(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum());
    }

    private void recalculateCartTotalCost(Cart cart) {
        cart.setTotalCost(cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
