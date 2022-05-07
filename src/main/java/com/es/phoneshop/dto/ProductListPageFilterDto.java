package com.es.phoneshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class ProductListPageFilterDto {
    private Optional<String> query;

    private Optional<String> sortField;

    private Optional<String> sortOrder;

}
