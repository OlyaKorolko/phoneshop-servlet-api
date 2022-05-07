package com.es.phoneshop.mapper;

import com.es.phoneshop.dto.ProductListPageFilterDto;
import com.es.phoneshop.enums.SortParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public final class ProductListPageFilterMapper {

    public ProductListPageFilterDto map(HttpServletRequest request) {
        Optional<String> query = Optional.ofNullable(
                request.getParameter(String.valueOf(SortParam.QUERY).toLowerCase())
        );
        Optional<String> sortField = Optional.ofNullable(
                request.getParameter(String.valueOf(SortParam.SORT).toLowerCase())
        );
        Optional<String> sortOrder = Optional.ofNullable(
                request.getParameter(String.valueOf(SortParam.ORDER).toLowerCase())
        );
        return new ProductListPageFilterDto(query, sortField, sortOrder);
    }

}
