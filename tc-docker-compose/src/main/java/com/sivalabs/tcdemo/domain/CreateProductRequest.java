package com.sivalabs.tcdemo.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProductRequest {

    private String code;

    private String name;

    private BigDecimal price;
}
