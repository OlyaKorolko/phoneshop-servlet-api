package com.es.phoneshop.service;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.impl.DefaultCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    private CartService cartService;
    private Cart cart;
    private CartItem cartItem;
    private Product product;
    private Product product2;

    @Before
    public void setUp() {
        when(request.getSession()).thenReturn(session);

        product = new Product("test", "", new BigDecimal(100), null, 100, null);
        product2=new Product("test2", "", new BigDecimal(200), null, 200, null);
        ProductDao productDao = ArrayListProductDao.getInstance();
        productDao.save(product);
        productDao.save(product2);
        cartService = DefaultCartService.getInstance();
        cart = new Cart();
        cart.getItems().add(new CartItem(product, 1));
    }

    @Test
    public void testGetNullCart() {
        when(session.getAttribute(any())).thenReturn(null);
        assertTrue(cartService.getCart(request).getItems().isEmpty());
    }

    @Test
    public void testGetCart() {
        when(session.getAttribute(any())).thenReturn(cart);
        assertEquals(1, cartService.getCart(request).getItems().size());
    }

    @Test
    public void testAdd() throws OutOfStockException {
        cartService.add(cart, 1L, 1);

        when(session.getAttribute(any())).thenReturn(cart);
        assertEquals(2, cartService.getCart(request).getItems().size());
    }

    @Test(expected = OutOfStockException.class)
    public void addOutOfStock() throws OutOfStockException {
        cartService.add(cart, 0L, 101);
    }
}