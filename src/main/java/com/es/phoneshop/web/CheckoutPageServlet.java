package com.es.phoneshop.web;

import com.es.phoneshop.dto.CheckoutPageDto;
import com.es.phoneshop.enums.param.CartParam;
import com.es.phoneshop.mapper.CheckoutPageMapper;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CheckoutPageServlet extends HttpServlet {
    private static final String CHECKOUT_PATH = "/WEB-INF/pages/checkout.jsp";
    private static final String ORDER_OVERVIEW_PATH = "/order/overview/";
    private CartService cartService;
    private OrderService orderService;
    private CheckoutPageMapper checkoutPageMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
        checkoutPageMapper = new CheckoutPageMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute(String.valueOf(CartParam.CART).toLowerCase(), cart);
        request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(),
                Order.builder()
                        .subtotalCost(cart.getTotalCost())
                        .deliveryCost(orderService.calculateDeliveryCost())
                        .build());
        request.setAttribute(checkoutPageMapper.parseParameter(CartParam.PAYMENT_METHODS), orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cart cart = cartService.getCart(request);
        CheckoutPageDto checkoutPageDto = checkoutPageMapper.map(request);
        placeOrder(request, response, checkoutPageDto, cart);
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response, CheckoutPageDto checkoutPageDto,
                            Cart cart) throws IOException, ServletException {
        if (checkoutPageDto.getErrors().isEmpty()) {
            Order order = orderService.placeOrder(checkoutPageDto, cart, request.getSession().getId());
            cartService.removeCart(request);
            response.sendRedirect(request.getContextPath() + ORDER_OVERVIEW_PATH + order.getSecureId());
        } else {
            request.setAttribute(String.valueOf(CartParam.ERRORS).toLowerCase(), checkoutPageDto.getErrors());
            request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), Order.builder().build());
            request.setAttribute(checkoutPageMapper.parseParameter(CartParam.PAYMENT_METHODS),
                    orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_PATH).forward(request, response);
        }
    }
}
