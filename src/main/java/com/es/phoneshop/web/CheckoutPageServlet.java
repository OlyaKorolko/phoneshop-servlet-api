package com.es.phoneshop.web;

import com.es.phoneshop.dto.CheckoutPageDto;
import com.es.phoneshop.enums.CartParam;
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
        request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), orderService.getOrder(cart));
        request.setAttribute(checkoutPageMapper.parseParameter(CartParam.PAYMENT_METHODS), orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        CheckoutPageDto checkoutPageDto = checkoutPageMapper.map(request);
        placeOrder(request, response, checkoutPageDto, order);
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response, CheckoutPageDto checkoutPageDto,
                            Order order) throws IOException, ServletException {
        if (checkoutPageDto.getErrors().isEmpty()) {
            setOrderInfo(order, checkoutPageDto);
            orderService.placeOrder(order);
            cartService.removeCart(request);
            response.sendRedirect(request.getContextPath() + ORDER_OVERVIEW_PATH + order.getSecureId());
        } else {
            request.setAttribute(String.valueOf(CartParam.ERRORS).toLowerCase(), checkoutPageDto.getErrors());
            request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), order);
            request.setAttribute(checkoutPageMapper.parseParameter(CartParam.PAYMENT_METHODS),
                    orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_PATH).forward(request, response);
        }
    }

    private void setOrderInfo(Order order, CheckoutPageDto checkoutPageDto) {
        checkoutPageDto.getFirstName().ifPresent(order::setFirstName);
        checkoutPageDto.getLastName().ifPresent(order::setLastName);
        checkoutPageDto.getPhone().ifPresent(order::setPhone);
        checkoutPageDto.getDeliveryDate().ifPresent(order::setDeliveryDate);
        checkoutPageDto.getDeliveryAddress().ifPresent(order::setDeliveryAddress);
        checkoutPageDto.getPaymentMethod().ifPresent(order::setPaymentMethod);
    }
}
