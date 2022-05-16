package com.es.phoneshop.mapper;

import com.es.phoneshop.dto.ProductDetailsPageDto;
import com.es.phoneshop.enums.param.ProductParam;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProductDetailsPageMapper {

    public ProductDetailsPageDto map(HttpServletRequest request) {
        return new ProductDetailsPageDto(parseQuantityString(request));
    }

    private int parseQuantityString(HttpServletRequest request) {
        String quantityString = request.getParameter(String.valueOf(ProductParam.QUANTITY).toLowerCase());
        ParsePosition parsePosition = new ParsePosition(0);
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        Number n = format.parse(quantityString, parsePosition);
        int quantity;
        if (n == null || quantityString.length() != parsePosition.getIndex()) {
            throw new NumberFormatException("Parsing failed: not a number");
        } else {
            quantity = n.intValue();
            if (quantity <= 0) {
                throw new NumberFormatException("Parsing failed: quantity should be >= 0");
            }
        }
        return quantity;
    }
}
