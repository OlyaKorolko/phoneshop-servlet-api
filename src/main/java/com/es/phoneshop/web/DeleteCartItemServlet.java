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
            request.setAttribute(String.valueOf(ProductParam.ERROR).toLowerCase(), "Parsing failed: invalid product number");
            response.sendError(500);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/cart?message=Cart item was successfully removed");
    }
}
