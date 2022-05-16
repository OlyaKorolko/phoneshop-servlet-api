package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLOrderDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLOrderItemDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLProductDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.enums.param.CartParam;
import com.es.phoneshop.enums.param.ProductParam;
import com.es.phoneshop.model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class OrderOverviewPageServlet extends HttpServlet {
    private static final String ORDER_OVERVIEW_PATH = "/WEB-INF/pages/orderOverview.jsp";
    private static final int ERROR_CODE = 500;
    private OrderDao orderDao;
    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDao = new MySQLOrderDao(new DBConnector(), new MySQLOrderItemDao(new DBConnector()));
        productDao = new MySQLProductDao(new DBConnector());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(1);
        Optional<Order> optionalOrder = orderDao.findBySecureId(secureOrderId);
        if (optionalOrder.isEmpty()) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), "Order was not found.");
            response.sendError(ERROR_CODE);
            return;
        }
        request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), optionalOrder.get());
        request.setAttribute(String.valueOf(CartParam.ITEMS).toLowerCase(),
                productDao.findByOrderId(optionalOrder.get().getId()));
        request.getRequestDispatcher(ORDER_OVERVIEW_PATH).forward(request, response);
    }
}
