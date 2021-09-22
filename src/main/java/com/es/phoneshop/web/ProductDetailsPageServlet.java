package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.ViewHistoryService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultViewHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private ViewHistoryService viewHistoryService;

    @Override
    public void init() throws ServletException {
        super.init();
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewHistoryService = DefaultViewHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId;
        try {
            productId = parseProductId(request);
        } catch (NumberFormatException ex) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), "Parsing failed: invalid product number");
            response.sendError(500);
            return;
        }
        request.setAttribute(String.valueOf(ProductParam.VIEW_HISTORY).toLowerCase(), viewHistoryService.getViewHistory(request));
        request.setAttribute(String.valueOf(ProductParam.PRODUCT).toLowerCase(), productDao.getProduct(productId));
        request.setAttribute(String.valueOf(ProductParam.CART).toLowerCase(), cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
        viewHistoryService.addToHistory(viewHistoryService.getViewHistory(request).getHistory(), productDao.getProduct(productId));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter(String.valueOf(ProductParam.QUANTITY).toLowerCase());
        int quantity;
        Long productId;
        try {
            productId = parseProductId(request);
            ParsePosition parsePosition = new ParsePosition(0);
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString, parsePosition).intValue();
            if (quantityString.length() != parsePosition.getIndex()) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), "Parsing failed: not a number");
            doGet(request, response);
            return;
        }
        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), "Out of stock, available: " + e.getStockAvailable());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}
