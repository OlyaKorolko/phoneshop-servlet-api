package com.es.phoneshop.model.cart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Cart implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<CartItem> items = new ArrayList<>();
    private int totalQuantity;
    private BigDecimal totalCost;
}
