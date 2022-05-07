package com.es.phoneshop.web;

import com.es.phoneshop.dto.AddToCartDto;
import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.mapper.AddToCartMapper;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddToCartServlet extends HttpServlet {
    public static final String CART_MESSAGE = "/products?message=Cart was successfully updated";
    public static final String PRODUCTS_PATH = "/products";
    private CartService cartService;
    private AddToCartMapper addToCartMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        addToCartMapper = new AddToCartMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        AddToCartDto addToCartDto = addToCartMapper.map(request);
        try {
            cartService.add(cart, addToCartDto.getProductId(), addToCartDto.getQuantity());
        } catch (OutOfStockException ex) {
            addToCartDto.getErrors().put(addToCartDto.getProductId(), ex.getMessage());
        }
        if (addToCartDto.getErrors().isEmpty()) {
            response.sendRedirect(request.getContextPath() + CART_MESSAGE);
        } else {
            request.setAttribute(String.valueOf(CartParam.ERRORS).toLowerCase(), addToCartDto.getErrors());
            request.getRequestDispatcher(PRODUCTS_PATH).forward(request, response);
        }
    }
}
