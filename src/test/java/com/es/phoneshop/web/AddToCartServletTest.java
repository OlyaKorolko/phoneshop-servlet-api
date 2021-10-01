package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
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
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AddToCartServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    HttpSession session;

    private AddToCartServlet servlet;

    @Before
    public void setUp() throws Exception {
        servlet = new AddToCartServlet();

        servlet.init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(any())).thenReturn(new Cart());

        ProductDao productDao = ArrayListProductDao.getInstance();
        productDao.save(new Product(null, null, new BigDecimal(100), null, 100, null));
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(new Locale("ENG"));
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getParameterValues(ProductParam.QUANTITY.name().toLowerCase())).thenReturn(new String[]{"1"});
        when(request.getParameterValues("productId")).thenReturn(new String[]{"0"});
        servlet.doPost(request, response);
        verify(response).sendRedirect(any());
    }
}