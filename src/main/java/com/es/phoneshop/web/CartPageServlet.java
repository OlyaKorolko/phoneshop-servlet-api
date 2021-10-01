package com.es.phoneshop.web;

import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String CART_PATH = "/WEB-INF/pages/cart.jsp";
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(String.valueOf(CartParam.CART).toLowerCase(), cartService.getCart(request));
        request.getRequestDispatcher(CART_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productsIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues(String.valueOf(ProductParam.QUANTITY).toLowerCase());
        Map<Long, String> errors = new HashMap<>();
        int quantity;
        long productId = -1L;
        for (int i = 0; i < productsIds.length; i++) {
            try {
                productId = Long.parseLong(productsIds[i]);
                quantity = parseQuantityString(request, quantities[i]);
                Cart cart = cartService.getCart(request);
                cartService.update(cart, productId, quantity);

            } catch (NumberFormatException | OutOfStockException ex) {
                errors.put(productId, ex.getMessage());
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart was successfully updated");
        } else {
            request.setAttribute(String.valueOf(CartParam.ERRORS).toLowerCase(), errors);
            doGet(request, response);
        }
    }

    private int parseQuantityString(HttpServletRequest request, String quantityString) {
        ParsePosition parsePosition = new ParsePosition(0);
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        Number n = format.parse(quantityString, parsePosition);
        int quantity;
        if (n == null || quantityString.length() != parsePosition.getIndex()) {
            throw new NumberFormatException("Parsing failed: not a number");
        } else {
            quantity = n.intValue();
            if (quantity <= 0) {
                throw new NumberFormatException("Parsing failed: quantity should be >= 0");
            }
        }
        return quantity;
    }
}
