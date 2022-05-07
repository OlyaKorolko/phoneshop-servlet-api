package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultCartService implements CartService {
    public static final String CART_SESSION_ATTRIBUTE = "cart";
    private static volatile CartService instance;
    private final ProductDao productDao = ArrayListProductDao.getInstance();

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
        synchronized (request.getSession()) {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        synchronized (cart) {
            Product product = productDao.getItem(productId);
            Optional<CartItem> foundCartItem = findCartItem(cart, product);
            int foundItemQuantityInCart = foundCartItem.map(CartItem::getQuantity).orElse(0);

            if (product.getStock() < quantity + foundItemQuantityInCart) {
                throw new OutOfStockException("Out of stock, available: " + product.getStock() +
                        ", requested: " + quantity + foundItemQuantityInCart);
            }
            if (foundCartItem.isPresent()) {
                foundCartItem.get().setQuantity(foundItemQuantityInCart + quantity);
            } else {
                CartItem cartItem = new CartItem(product, quantity);
                cart.getItems().add(cartItem);
            }
            recalculateCart(cart);
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
        synchronized (cart) {
            Product product = productDao.getItem(productId);
            Optional<CartItem> foundCartItem = findCartItem(cart, product);

            if (product.getStock() < quantity) {
                throw new OutOfStockException("Out of stock, available: " + product.getStock() + ", requested: " + quantity);
            }
            if (foundCartItem.isPresent()) {
                foundCartItem.get().setQuantity(quantity);
            } else {
                CartItem cartItem = new CartItem(product, quantity);
                cart.getItems().add(cartItem);
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void delete(Cart cart, Long productId) {
        synchronized (cart) {
            cart.getItems().removeIf(cartItem -> cartItem.getProduct().getId().equals(productId));
            recalculateCart(cart);
        }
    }

    @Override
    public void removeCart(HttpServletRequest request) {
        synchronized (request.getSession()) {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart != null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, null);
            }
        }
    }

    @Override
    public void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct() != null)
                .mapToInt(CartItem::getQuantity)
                .sum());
        cart.setTotalCost(cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct() != null)
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
