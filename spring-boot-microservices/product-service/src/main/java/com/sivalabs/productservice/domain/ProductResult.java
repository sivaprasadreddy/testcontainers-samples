package com.sivalabs.productservice.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResult {
    private Long id;
    private String name;
    private BigDecimal originalPrice;
    private BigDecimal discount;
    private BigDecimal price;
}
