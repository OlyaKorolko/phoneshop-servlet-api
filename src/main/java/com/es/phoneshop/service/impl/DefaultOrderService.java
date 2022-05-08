package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLOrderDao;
import com.es.phoneshop.dao.impl.my_sql.MySQLOrderItemDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.dto.CheckoutPageDto;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultOrderService implements OrderService {
    private final OrderDao orderDao = new MySQLOrderDao(new DBConnector(), new MySQLOrderItemDao(new DBConnector()));
    private static volatile OrderService instance;

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            synchronized (DefaultOrderService.class) {
                if (instance == null) {
                    instance = new DefaultOrderService();
                }
            }
        }
        return instance;
    }

    private Order createOrder(CheckoutPageDto checkoutPageDto, Cart cart) {
        Order order = Order.builder().build();

        checkoutPageDto.getFirstName().ifPresent(order::setFirstName);
        checkoutPageDto.getLastName().ifPresent(order::setLastName);
        checkoutPageDto.getPhone().ifPresent(order::setPhone);
        checkoutPageDto.getDeliveryDate().ifPresent(order::setDeliveryDate);
        checkoutPageDto.getDeliveryAddress().ifPresent(order::setDeliveryAddress);
        checkoutPageDto.getPaymentMethod().ifPresent(order::setPaymentMethod);

        if (!cart.getItems().isEmpty()) {
            order.setSubtotalCost(cart.getTotalCost());
            order.setDeliveryCost(calculateDeliveryCost());
        }
        return order;
    }

    @Override
    public BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public Order placeOrder(CheckoutPageDto checkoutPageDto, Cart cart, String sessionId) {
        Order order = createOrder(checkoutPageDto, cart);
        order.setSecureId(UUID.randomUUID().toString());
        order.setSessionId(sessionId);
        orderDao.save(order, cart.getItems());
        return order;
    }
}
