package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_OVERVIEW_PATH = "/WEB-INF/pages/orderOverview.jsp";
    private OrderDao orderDao;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(1);
        Order order;
        try {
            order = orderDao.getOrderBySecureId(secureOrderId);
        } catch (OrderNotFoundException e) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), e.getMessage());
            response.sendError(500);
            return;
        }
        request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), order);
        request.getRequestDispatcher(ORDER_OVERVIEW_PATH).forward(request, response);
    }

}
