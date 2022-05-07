package com.es.phoneshop.service;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.DefaultOrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    private OrderService orderService;
    private Cart cart;
    private Product product;
    private OrderDao orderDao;

    @Before
    public void setUp() {
        product = new Product("test", "", new BigDecimal(100), null, 100, null);
        cart = new Cart();
        cart.getItems().add(new CartItem(product, 1));
        cart.setTotalCost(product.getPrice());
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void testGetOrder() {
        Order order = orderService.getOrder(cart);
        assertEquals(1, order.getItems().size());
        assertEquals(product.getId(), order.getItems().get(0).getProduct().getId());
        assertEquals(BigDecimal.valueOf(100), order.getSubtotalCost());
    }

    @Test
    public void testGetOrderEmptyCart() {
        Order order = orderService.getOrder(new Cart());
        assertEquals(0, order.getItems().size());
        assertEquals(BigDecimal.ZERO, order.getSubtotalCost());
    }

    @Test
    public void testPlaceOrder() {
        Order newOrder = new Order();
        newOrder.setItems(cart.getItems());
        orderService.placeOrder(newOrder);
        assertNotNull(newOrder.getSecureId());
        assertEquals(newOrder.getId(), orderDao.getOrderBySecureId(newOrder.getSecureId()).getId());
    }
}