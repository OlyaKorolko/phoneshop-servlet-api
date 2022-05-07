package com.es.phoneshop.web;

import com.es.phoneshop.dto.CartPageDto;
import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.mapper.CartPageMapper;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private static final String CART_PATH = "/WEB-INF/pages/cart.jsp";
    private static final String CART_MESSAGE = "/cart?message=Cart was successfully updated";
    private CartService cartService;
    private CartPageMapper cartPageMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        cartPageMapper = new CartPageMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(String.valueOf(CartParam.CART).toLowerCase(), cartService.getCart(request));
        request.getRequestDispatcher(CART_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        CartPageDto cartPageDto = cartPageMapper.map(request);
        Map<Long, String> errors = findErrors(cartPageDto, cart);

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + CART_MESSAGE);
        } else {
            request.setAttribute(String.valueOf(CartParam.ERRORS).toLowerCase(), errors);
            doGet(request, response);
        }
    }

    Map<Long, String> findErrors(CartPageDto cartPageDto, Cart cart) {
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < cartPageDto.getProductIds().size(); i++) {
            try {
                cartService.update(cart, cartPageDto.getProductIds().get(i), cartPageDto.getQuantities().get(i));
            } catch (NumberFormatException | OutOfStockException ex) {
                errors.put(cartPageDto.getProductIds().get(i), ex.getMessage());
            }
        }
        return errors;
    }
}
