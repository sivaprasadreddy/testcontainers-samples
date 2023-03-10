package com.sivalabs.productservice.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Promotion {

	private Long id;

	private Long productId;

	private BigDecimal discount;

}
