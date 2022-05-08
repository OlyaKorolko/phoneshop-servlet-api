package com.es.phoneshop.service;

import com.es.phoneshop.dto.CheckoutPageDto;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order placeOrder(CheckoutPageDto checkoutPageDto, Cart cart, String sessionId);

    List<PaymentMethod> getPaymentMethods();

    BigDecimal calculateDeliveryCost();
}
