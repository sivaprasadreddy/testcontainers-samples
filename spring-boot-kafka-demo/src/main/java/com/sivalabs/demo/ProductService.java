package com.sivalabs.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;

	public void updateProductPrice(String productCode, BigDecimal price) {
		repository.updateProductPrice(productCode, price);
	}

}