package com.sivalabs.tcdemo.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreateProductRequest {

	private String code;

	private String name;

	private BigDecimal price;

}
