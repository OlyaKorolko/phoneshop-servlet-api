package com.es.phoneshop.mapper;

import com.es.phoneshop.dto.AddToCartDto;
import com.es.phoneshop.enums.ProductParam;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

public class AddToCartMapper {
    private static final String PRODUCT_ID = "productId";
    private static final String PARSING_ERROR_1 = "Parsing failed: not a number";
    private static final String PARSING_ERROR_2 = "Parsing failed: quantity should be >= 0";

    public AddToCartDto map(HttpServletRequest request) {
        Map<Long, String> errors = new HashMap<>();
        int quantity = -1;
        long productId = -1L;
        String quantityString;
        try {
            String productIdString = request.getPathInfo().substring(1);
            String[] quantityStrings = request.getParameterValues(String.valueOf(ProductParam.QUANTITY).toLowerCase());
            productId = Long.parseLong(productIdString);
            quantityString = findQuantityString(request.getParameterValues(PRODUCT_ID), quantityStrings, productIdString);
            quantity = parseQuantityString(request, quantityString);
        } catch (NumberFormatException e) {
            errors.put(productId, e.getMessage());
        }
        return new AddToCartDto(quantity, productId, errors);
    }

    private int parseQuantityString(HttpServletRequest request, String quantityString) {
        ParsePosition parsePosition = new ParsePosition(0);
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        Number quantityNumber = format.parse(quantityString, parsePosition);
        int quantity;
        if (quantityNumber == null || quantityString.length() != parsePosition.getIndex()) {
            throw new NumberFormatException(PARSING_ERROR_1);
        } else {
            quantity = quantityNumber.intValue();
            if (quantity <= 0) {
                throw new NumberFormatException(PARSING_ERROR_2);
            }
        }
        return quantity;
    }

    private String findQuantityString(String[] productsIdsString, String[] quantityStrings, String productIdString) {
        String quantityString = null;
        for (int i = 0; i < productsIdsString.length; i++) {
            if (productsIdsString[i].equals(productIdString)) {
                quantityString = quantityStrings[i];
            }
        }
        return quantityString;
    }
}
