package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    private OrderOverviewPageServlet servlet;
    private final UUID id = UUID.randomUUID();
    private Order order;

    @Before
    public void setUp() throws Exception {
        servlet = new OrderOverviewPageServlet();
        order = new Order();
        order.setSecureId(String.valueOf(id));

        servlet.init();
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        OrderDao orderDao = ArrayListOrderDao.getInstance();
        orderDao.save(order);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/" + id);
        servlet.doGet(request, response);
        verify(request).setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), order);
        verify(requestDispatcher).forward(request, response);
    }

}