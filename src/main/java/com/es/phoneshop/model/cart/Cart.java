package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.BaseEntity;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class Cart extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<CartItem> items = new ArrayList<>();
    private int totalQuantity;
    private BigDecimal totalCost;
}
