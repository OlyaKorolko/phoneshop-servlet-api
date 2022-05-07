package com.es.phoneshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class AddToCartDto {
    private int quantity;
    private long productId;
    private Map<Long, String> errors;
}
