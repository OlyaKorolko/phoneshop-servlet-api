package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.my_sql.MySQLProductDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.enums.param.ProductParam;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.impl.DefaultProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private static final String PRICE_HISTORY_PATH = "/WEB-INF/pages/productPriceHistory.jsp";
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new DefaultProductService(new MySQLProductDao(new DBConnector()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(String.valueOf(ProductParam.PRODUCT).toLowerCase(), getProduct(request));
        request.getRequestDispatcher(PRICE_HISTORY_PATH).forward(request, response);
    }

    private Product getProduct(HttpServletRequest request) {
        return productService.findById(Long.valueOf(request.getPathInfo().substring(1)));
    }
}
