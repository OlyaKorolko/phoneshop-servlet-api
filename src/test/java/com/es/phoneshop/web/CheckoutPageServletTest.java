package com.es.phoneshop.web;

import com.es.phoneshop.enums.CartParam;
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
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;

    private CheckoutPageServlet servlet;

    @Before
    public void setUp() throws Exception {
        servlet = new CheckoutPageServlet();
        servlet.init();
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq(String.valueOf(CartParam.ORDER).toLowerCase()), any());
        verify(request).setAttribute(eq("paymentMethods"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("cnejd");
        when(request.getParameter("lastName")).thenReturn("csdjk");
        when(request.getParameter("phone")).thenReturn("46328944");
        when(request.getParameter("deliveryDate")).thenReturn("2007-12-23");
        when(request.getParameter("deliveryAddress")).thenReturn("cknd");
        when(request.getParameter("paymentMethod")).thenReturn("CASH");
        servlet.doPost(request, response);
        verify(response).sendRedirect(any());
    }
}