package com.es.phoneshop.model.order;

import com.es.phoneshop.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode
@Builder
@Data
public class Order {
    private Long id;
    private String secureId;
    private BigDecimal subtotalCost;
    private BigDecimal deliveryCost;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate deliveryDate;
    private String deliveryAddress;
    private PaymentMethod paymentMethod;
    private String sessionId;
}
