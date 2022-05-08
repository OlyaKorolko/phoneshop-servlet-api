package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.my_sql.MySQLProductDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.dto.ProductDetailsPageDto;
import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.mapper.ProductDetailsPageMapper;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.ProductService;
import com.es.phoneshop.service.ViewHistoryService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultProductService;
import com.es.phoneshop.service.impl.DefaultViewHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private static final String PRODUCT_DETAILS_PATH = "/WEB-INF/pages/product.jsp";
    private static final String ERROR_MESSAGE = "Parsing failed: invalid product number";
    private static final String PRODUCTS_PATH_1 = "/products?message=Product ";
    private static final String PRODUCTS_PATH_2 = " was added to cart";
    private static final int ERROR_CODE = 500;
    private ProductService productService;
    private CartService cartService;
    private ViewHistoryService viewHistoryService;
    private ProductDetailsPageMapper productDetailsPageMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new DefaultProductService(new MySQLProductDao(new DBConnector()));
        cartService = DefaultCartService.getInstance();
        viewHistoryService = DefaultViewHistoryService.getInstance();
        productDetailsPageMapper = new ProductDetailsPageMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId;
        try {
            productId = parseProductId(request);
        } catch (NumberFormatException ex) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), ERROR_MESSAGE);
            response.sendError(ERROR_CODE);
            return;
        }
        request.setAttribute(String.valueOf(ProductParam.VIEW_HISTORY).toLowerCase(),
                viewHistoryService.getViewHistory(request));
        request.setAttribute(String.valueOf(ProductParam.PRODUCT).toLowerCase(),
                productService.findById(productId));
        request.setAttribute(String.valueOf(CartParam.CART).toLowerCase(),
                cartService.getCart(request));
        request.getRequestDispatcher(PRODUCT_DETAILS_PATH).forward(request, response);
        viewHistoryService.addToHistory(viewHistoryService.getViewHistory(request).getHistory(),
                productService.findById(productId));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long productId;
        ProductDetailsPageDto productDetailsPageDto;
        try {
            productId = parseProductId(request);
            productDetailsPageDto = productDetailsPageMapper.map(request);
            Cart cart = cartService.getCart(request);
            cartService.add(cart, productId, productDetailsPageDto.getQuantity());
        } catch (NumberFormatException | OutOfStockException ex) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), ex.getMessage());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + PRODUCTS_PATH_1 + productId + PRODUCTS_PATH_2);
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}
