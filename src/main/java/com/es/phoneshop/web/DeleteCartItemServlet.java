package com.es.phoneshop.web;

import com.es.phoneshop.enums.ProductParam;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {
    private static final String ERROR_MESSAGE = "Parsing failed: invalid product number";
    private static final String CART_MESSAGE = "/cart?message=Cart item was successfully removed";
    private static final int ERROR_CODE = 500;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productsId = request.getPathInfo().substring(1);
        Cart cart = cartService.getCart(request);
        try {
            cartService.delete(cart, Long.parseLong(productsId));
        } catch (NumberFormatException e) {
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), ERROR_MESSAGE);
            response.sendError(ERROR_CODE);
            return;
        }
        response.sendRedirect(request.getContextPath() + CART_MESSAGE);
    }
}
