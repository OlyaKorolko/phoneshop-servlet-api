package com.es.phoneshop.dao;

import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;
    Order order;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        order = new Order();
        order.setSecureId("11");
        order.setItems(Arrays.asList(
                new CartItem(new Product(1L, "test1", "", new BigDecimal(100), null, 100, null), 10),
                new CartItem(new Product(2L, "test2", "", new BigDecimal(200), null, 140, null), 20),
                new CartItem(new Product(3L, "test3", "", new BigDecimal(300), null, 5, null), 50)));
        if (orderDao.findOrders().isEmpty()) {
            orderDao.save(order);
        }
    }

    @Test
    public void testGetInstance() {
        OrderDao od = ArrayListOrderDao.getInstance();
        assertSame(orderDao, od);
    }

    @Test
    public void testSaveOrder() {
        Order o = new Order();
        orderDao.save(o);
        assertEquals(1L, o.getId().longValue());
    }

    @Test
    public void testGetOrder() {
        assertEquals(order.getId(), orderDao.getItem(0L).getId());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderNotFound() {
        orderDao.getItem(12L);
    }

    @Test
    public void testGetOrderBySecureId() {
        assertEquals(0L, orderDao.getOrderBySecureId("11").getId().longValue());
    }

}