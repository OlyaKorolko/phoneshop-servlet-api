package com.es.phoneshop.web;

import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.model.cart.Cart;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    HttpSession session;

    private MiniCartServlet miniCartServlet;

    @Before
    public void setUp() throws Exception {
        miniCartServlet = new MiniCartServlet();

        miniCartServlet.init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(any())).thenReturn(new Cart());
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        miniCartServlet.doGet(request, response);
        verify(requestDispatcher).include(request, response);
        verify(request).setAttribute(eq(String.valueOf((CartParam.CART)).toLowerCase()), any());
    }
}