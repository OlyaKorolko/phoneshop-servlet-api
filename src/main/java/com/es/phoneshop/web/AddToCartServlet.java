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

public class AddToCartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdString = request.getPathInfo().substring(1);
        String[] productsIdsString = request.getParameterValues("productId");
        String[] quantityStrings = request.getParameterValues(String.valueOf(ProductParam.QUANTITY).toLowerCase());
        String quantityString;
        Map<Long, String> errors = new HashMap<>();
        int quantity;
        long productId = -1L;
        try {
            productId = Long.parseLong(productIdString);
            quantityString = findQuantityString(productsIdsString, quantityStrings, productIdString);
            quantity = parseQuantityString(request, quantityString);
            Cart cart = cartService.getCart(request);
            cartService.add(cart, productId, quantity);
        } catch (NumberFormatException | OutOfStockException ex) {
            errors.put(productId, ex.getMessage());
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products?message=Cart was successfully updated");
        } else {
            request.setAttribute(String.valueOf(CartParam.ERRORS).toLowerCase(), errors);
            request.getRequestDispatcher("/products").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private int parseQuantityString(HttpServletRequest request, String quantityString) {
        ParsePosition parsePosition = new ParsePosition(0);
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        Number quantityNumber = format.parse(quantityString, parsePosition);
        int quantity;
        if (quantityNumber == null || quantityString.length() != parsePosition.getIndex()) {
            throw new NumberFormatException("Parsing failed: not a number");
        } else {
            quantity = quantityNumber.intValue();
            if (quantity <= 0) {
                throw new NumberFormatException("Parsing failed: quantity should be >= 0");
            }
        }
        return quantity;
    }

    private String findQuantityString(String[] productsIdsString, String[] quantityStrings, String productIdString) {
        String quantityString = null;
        for (int i = 0; i < productsIdsString.length; i++) {
            if (productsIdsString[i].equals(productIdString)) {
                quantityString = quantityStrings[i];
            }
        }
        return quantityString;
    }
}
