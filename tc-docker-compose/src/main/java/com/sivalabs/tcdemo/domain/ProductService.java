package com.sivalabs.tcdemo.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public Optional<Product> getByCode(String code) {
		log.info("Fetching product by code:{}", code);
		return productRepository.findByCode(code);
	}

	public void save(Product product) {
		productRepository.save(product);
	}

}
