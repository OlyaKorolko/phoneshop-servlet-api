package com.es.phoneshop.dto;

import com.es.phoneshop.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class CheckoutPageDto {
    private Map<String, String> errors;
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> phone;
    private Optional<LocalDate> deliveryDate;
    private Optional<String> deliveryAddress;
    private Optional<PaymentMethod> paymentMethod;
}
