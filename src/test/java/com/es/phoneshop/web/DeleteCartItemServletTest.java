package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    HttpSession session;

    private DeleteCartItemServlet servlet;
    private Cart cart;

    @Before
    public void setUp() throws Exception {
        servlet = new DeleteCartItemServlet();
        cart = new Cart();
        CartItem cartItem = new CartItem(new Product(0L, null, null, new BigDecimal(100), null, 100, null), 20);
        cart.getItems().add(cartItem);
        servlet.init();
        when(request.getSession()).thenReturn(session);

        ProductDao productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product(null, null, new BigDecimal(100), null, 100, null));
    }

    @Test
    public void testDoPost() throws IOException {
        when(session.getAttribute(any())).thenReturn(cart);
        when(request.getPathInfo()).thenReturn("/0");

        servlet.doPost(request,response);
        verify(response).sendRedirect(any());
    }
}