package com.es.phoneshop.mapper;

import com.es.phoneshop.dto.CheckoutPageDto;
import com.es.phoneshop.enums.param.OrderParam;
import com.es.phoneshop.enums.PaymentMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CheckoutPageMapper {
    private static final String PHONE_PATTERN = "(\\+?(\\d){8,})|((\\d){2,}-?\\s?){3,}?";

    public CheckoutPageDto map(HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        Optional<String> firstName = parseRequiredParameter(request, OrderParam.FIRST_NAME, errors);
        Optional<String> lastName = parseRequiredParameter(request, OrderParam.LAST_NAME, errors);
        Optional<String> phone = parseRequiredParameter(request, OrderParam.PHONE, errors);
        Optional<LocalDate> deliveryDate = validateDeliveryDate(request, errors);
        Optional<String> deliveryAddress = parseRequiredParameter(request, OrderParam.DELIVERY_ADDRESS, errors);
        Optional<PaymentMethod> paymentMethod = validatePaymentMethod(request, errors);
        return new CheckoutPageDto(errors, firstName, lastName, phone, deliveryDate, deliveryAddress, paymentMethod);
    }

    public String parseParameter(final Enum<?> param) {
        String enumParam = String.valueOf(param).toLowerCase();
        String[] enumParamWords = enumParam.split("_");
        if (enumParamWords.length > 1) {
            for (int i = 1; i < enumParamWords.length; i++) {
                enumParamWords[i] = (char) (enumParamWords[i].charAt(0) - 32) + enumParamWords[i].substring(1);
            }
        }
        return Arrays.stream(enumParamWords).reduce(String::concat).orElse("");
    }

    private Optional<String> parseRequiredParameter(HttpServletRequest request, OrderParam orderParam,
                                                    Map<String, String> errors) {
        String parameter = parseParameter(orderParam);
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "This field is required");
            return Optional.empty();
        }
        if (orderParam.equals(OrderParam.PHONE) && !value.matches(PHONE_PATTERN)) {
            errors.put(parameter, "Phone number is invalid");
            return Optional.empty();
        }
        return Optional.of(value);
    }

    private Optional<LocalDate> validateDeliveryDate(HttpServletRequest request, Map<String, String> errors) {
        String parameter = parseParameter(OrderParam.DELIVERY_DATE);
        String value = request.getParameter(parameter);
        try {
            if (value == null || value.isEmpty()) {
                errors.put(parameter, "Delivery date is required");
                return Optional.empty();
            } else {
                LocalDate dateValue = LocalDate.parse(value);
                return Optional.of(dateValue);
            }
        } catch (DateTimeParseException e) {
            errors.put(parameter, "Invalid delivery date");
        } catch (IllegalArgumentException e) {
            errors.put(parameter, e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<PaymentMethod> validatePaymentMethod(HttpServletRequest request, Map<String, String> errors) {
        try {
            String parameter = parseParameter(OrderParam.PAYMENT_METHOD);
            String value = request.getParameter(parameter);
            if (value == null || value.isEmpty()) {
                errors.put(parameter, "Payment method is required");
                return Optional.empty();
            }
            return Optional.of(PaymentMethod.valueOf(value));
        } catch (IllegalArgumentException e) {
            errors.put(parseParameter(OrderParam.PAYMENT_METHOD), "Invalid payment method");
        }
        return Optional.empty();
    }
}
