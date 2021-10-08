package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.AdvancedSortParam;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.enums.Selector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdvancedSearchServlet extends HttpServlet {
    private static final String SEARCH_PATH = "/WEB-INF/pages/advancedSearch.jsp";
    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(String.valueOf(AdvancedSortParam.SELECTORS).toLowerCase(), getSelectorValues());
        request.getRequestDispatcher(SEARCH_PATH).forward(request, response);
    }

    private List<String> getSelectorValues() {
        return Arrays.stream(Selector.values()).map(value -> value.toString().replaceAll("_", " ").toLowerCase()).collect(Collectors.toList());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descriptionQuery = request.getParameter(String.valueOf(AdvancedSortParam.DESCRIPTION).toLowerCase());
        String wordSelector = request.getParameter(String.valueOf(AdvancedSortParam.SELECTOR).toLowerCase());
        String minPrice = request.getParameter(String.valueOf(AdvancedSortParam.MIN_PRICE).toLowerCase());
        String maxPrice = request.getParameter(String.valueOf(AdvancedSortParam.MAX_PRICE).toLowerCase());

        BigDecimal min = null;
        BigDecimal max = null;
        if (minPrice.matches("\\d+")) {
            min = BigDecimal.valueOf(Long.parseLong(minPrice));
        }
        if (maxPrice.matches("\\d+")) {
            max = BigDecimal.valueOf(Long.parseLong(maxPrice));
        }
        request.setAttribute(String.valueOf(ProductParam.PRODUCTS).toLowerCase(),
                productDao.findProducts(descriptionQuery, getSortParam(wordSelector), min, max));
        doGet(request, response);
    }

    private Selector getSortParam(String wordSelector) {
        return Selector.valueOf(wordSelector.replaceAll(" ", "_").toUpperCase());
    }
}
