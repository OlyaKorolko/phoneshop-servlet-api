package com.es.phoneshop.service;

import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    Order getOrder(Cart cart);

    void placeOrder(Order order);

    List<PaymentMethod> getPaymentMethods();
}
