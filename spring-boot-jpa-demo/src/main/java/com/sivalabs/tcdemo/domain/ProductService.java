package com.sivalabs.tcdemo.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public List<Product> getAllProducts() {
		return productRepository.findAll().stream().filter(p -> !p.isDisabled()).toList();
	}

	@Transactional(readOnly = true)
	public List<Product> getActiveProducts() {
		return productRepository.findAllActiveProducts();
	}

}
