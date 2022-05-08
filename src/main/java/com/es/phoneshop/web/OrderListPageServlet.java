package com.es.phoneshop.web;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLOrderDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLOrderItemDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLProductDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderListPageServlet extends HttpServlet {
    private static final String ORDERS_PATH = "/WEB-INF/pages/orderList.jsp";
    private OrderDao orderDao;
    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        orderDao = new MySQLOrderDao(new DBConnector(), new MySQLOrderItemDao(new DBConnector()));
        productDao = new MySQLProductDao(new DBConnector());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Order, List<CartItem>> orderListMap = orderDao.findAll(req.getSession().getId()).stream()
                .map(order -> Map.entry(order, productDao.findByOrderId(order.getId())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        req.setAttribute(String.valueOf(CartParam.ITEMS).toLowerCase(), orderListMap);
        req.getRequestDispatcher(ORDERS_PATH).forward(req, resp);
    }
}
