package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.model.product.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private static final String PRICE_HISTORY_PATH = "/WEB-INF/pages/productPriceHistory.jsp";
    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(String.valueOf(ProductParam.PRODUCT).toLowerCase(), getProduct(request));
        request.getRequestDispatcher(PRICE_HISTORY_PATH).forward(request, response);
    }

    private Product getProduct(HttpServletRequest request) {
        return productDao.getItem(Long.valueOf(request.getPathInfo().substring(1)));
    }
}
