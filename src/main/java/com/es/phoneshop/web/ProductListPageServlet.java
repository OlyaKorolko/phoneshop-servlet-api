package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.enums.SortParam;
import com.es.phoneshop.service.ViewHistoryService;
import com.es.phoneshop.service.impl.DefaultViewHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private static final String PRODUCTS_PATH = "/WEB-INF/pages/productList.jsp";
    private ProductDao productDao;
    private ViewHistoryService viewHistoryService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
        viewHistoryService = DefaultViewHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter(String.valueOf(SortParam.QUERY).toLowerCase());
        String sortField = request.getParameter(String.valueOf(SortParam.SORT).toLowerCase());
        String sortOrder = request.getParameter(String.valueOf(SortParam.ORDER).toLowerCase());
        request.setAttribute(String.valueOf(ProductParam.PRODUCTS).toLowerCase(), productDao.findProducts(query, sortField, sortOrder));
        request.setAttribute(String.valueOf(ProductParam.VIEW_HISTORY).toLowerCase(), viewHistoryService.getViewHistory(request));
        request.getRequestDispatcher(PRODUCTS_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
