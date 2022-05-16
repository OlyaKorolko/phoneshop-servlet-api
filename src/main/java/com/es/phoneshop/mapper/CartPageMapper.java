package com.es.phoneshop.mapper;

import com.es.phoneshop.dto.CartPageDto;
import com.es.phoneshop.enums.param.ProductParam;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;

public class CartPageMapper {
    private static final String PRODUCT_ID = "productId";
    private static final String PARSING_ERROR_1 = "Parsing failed: not a number";
    private static final String PARSING_ERROR_2 = "Parsing failed: quantity should be >= 0";

    public CartPageDto map(HttpServletRequest request) {
        List<Long> productsIds = Arrays.stream(request.getParameterValues(PRODUCT_ID)).map(Long::parseLong).toList();
        List<Integer> quantities = Arrays.stream(request.getParameterValues(String.valueOf(ProductParam.QUANTITY).toLowerCase()))
                .map(quantity -> parseQuantityString(request, quantity)).toList();
        return new CartPageDto(productsIds, quantities);
    }

    private int parseQuantityString(HttpServletRequest request, String quantityString) {
        ParsePosition parsePosition = new ParsePosition(0);
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        Number n = format.parse(quantityString, parsePosition);
        int quantity;
        if (n == null || quantityString.length() != parsePosition.getIndex()) {
            throw new NumberFormatException(PARSING_ERROR_1);
        } else {
            quantity = n.intValue();
            if (quantity <= 0) {
                throw new NumberFormatException(PARSING_ERROR_2);
            }
        }
        return quantity;
    }
}
