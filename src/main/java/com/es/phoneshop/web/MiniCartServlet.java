package com.es.phoneshop.web;

import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MiniCartServlet extends HttpServlet {
    private static final String MINI_CART_PATH = "/WEB-INF/pages/minicart.jsp";
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(String.valueOf(CartParam.CART).toLowerCase(), cartService.getCart(request));
        request.getRequestDispatcher(MINI_CART_PATH).include(request, response);
    }


}
