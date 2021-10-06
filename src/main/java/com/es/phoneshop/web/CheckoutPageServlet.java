package com.es.phoneshop.web;

import com.es.phoneshop.enums.CartParam;
import com.es.phoneshop.enums.OrderParam;
import com.es.phoneshop.enums.PaymentMethod;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private static final String CHECKOUT_PATH = "/WEB-INF/pages/checkout.jsp";
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), orderService.getOrder(cart));
        request.setAttribute(parseParameter(CartParam.PAYMENT_METHODS), orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        setRequiredParameter(request, parseParameter(OrderParam.FIRST_NAME), errors, order::setFirstName);
        setRequiredParameter(request, parseParameter(OrderParam.LAST_NAME), errors, order::setLastName);
        setRequiredParameter(request, String.valueOf(OrderParam.PHONE).toLowerCase(), errors, order::setPhone);
        setDeliveryDate(request, parseParameter(OrderParam.DELIVERY_DATE), errors, order::setDeliveryDate);
        setRequiredParameter(request, parseParameter(OrderParam.DELIVERY_ADDRESS), errors,
                order::setDeliveryAddress);
        setPaymentMethod(request, parseParameter(OrderParam.PAYMENT_METHOD), errors, order);

        placeOrder(request, response, errors, order);
    }

    private String parseParameter(final Enum<?> param) {
        String enumParam = String.valueOf(param).toLowerCase();
        String[] enumParamWords = enumParam.split("_");
        for (int i = 1; i < enumParamWords.length; i++) {
            enumParamWords[i] = (char) (enumParamWords[i].charAt(0) - 32) + enumParamWords[i].substring(1);
        }
        return Arrays.stream(enumParamWords).reduce(String::concat).orElse("");
    }

    public void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "This field is required");
        } else {
            if (parameter.equals(String.valueOf(OrderParam.PHONE).toLowerCase()) &&
                    !value.matches("(\\+?(\\d){8,})|((\\d){2,}-?\\s?){3,}?")) {
                errors.put(parameter, "Phone number is invalid");
            }
            consumer.accept(value);
        }
    }

    private void setDeliveryDate(HttpServletRequest request, String parameter, Map<String, String> errors,
                                 Consumer<LocalDate> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Delivery date is required");
        } else {
            try {
                LocalDate dateValue = LocalDate.parse(value);
                if (dateValue.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("Date cannot be earlier than now");
                }
                consumer.accept(dateValue);
            } catch (DateTimeParseException e) {
                errors.put(parameter, "Invalid delivery date");
            } catch (IllegalArgumentException e) {
                errors.put(parameter, e.getMessage());
            }
        }
    }

    private void setPaymentMethod(HttpServletRequest request, String parameter, Map<String, String> errors, Order order) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Payment method is required");
        } else {
            try {
                order.setPaymentMethod(PaymentMethod.valueOf(value));
            } catch (IllegalArgumentException e) {
                errors.put(parseParameter(OrderParam.PAYMENT_METHOD), "Invalid payment method");
            }
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors,
                              Order order) throws IOException, ServletException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.removeCart(request);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute(String.valueOf(CartParam.ERRORS).toLowerCase(), errors);
            request.setAttribute(String.valueOf(CartParam.ORDER).toLowerCase(), order);
            request.setAttribute(parseParameter(CartParam.PAYMENT_METHODS), orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_PATH).forward(request, response);
        }
    }
}
