package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.my_sql.MySQLProductDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.dto.ProductListPageFilterDto;
import com.es.phoneshop.enums.param.ProductParam;
import com.es.phoneshop.mapper.ProductListPageFilterMapper;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.ViewHistoryService;
import com.es.phoneshop.service.impl.DefaultProductService;
import com.es.phoneshop.service.impl.DefaultViewHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private static final String PRODUCTS_PATH = "/WEB-INF/pages/productList.jsp";
    private ProductService productService;
    private ViewHistoryService viewHistoryService;
    ProductListPageFilterMapper mapper;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new DefaultProductService(new MySQLProductDao(new DBConnector()));
        viewHistoryService = DefaultViewHistoryService.getInstance();
        mapper = new ProductListPageFilterMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductListPageFilterDto productListPageFilterDto = mapper.map(request);

        request.setAttribute(String.valueOf(ProductParam.PRODUCTS).toLowerCase(),
                productService.findByFilter(productListPageFilterDto));
        request.setAttribute(String.valueOf(ProductParam.VIEW_HISTORY).toLowerCase(),
                viewHistoryService.getViewHistory(request));

        request.getRequestDispatcher(PRODUCTS_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
