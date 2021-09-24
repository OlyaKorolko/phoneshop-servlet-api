package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    HttpSession session;

    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
    private final ProductDao productDao = ArrayListProductDao.getInstance();

    @Before
    public void setup() throws ServletException {
        servlet.init();
        productDao.save(new Product(null, null, new BigDecimal(100), null, 100, null));

        when(request.getPathInfo()).thenReturn("/0");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), any());
        verify(request).setAttribute(eq("cart"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getLocale()).thenReturn(Locale.ROOT);
        when(request.getParameter("quantity")).thenReturn("1");
        servlet.doPost(request, response);
        verify(response).sendRedirect(anyString());
    }
}