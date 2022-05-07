package com.es.phoneshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartPageDto {
    private List<Long> productIds;
    private List<Integer> quantities;
}
