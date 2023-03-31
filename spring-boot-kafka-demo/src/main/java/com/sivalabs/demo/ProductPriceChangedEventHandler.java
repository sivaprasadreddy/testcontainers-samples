package com.sivalabs.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductPriceChangedEventHandler {

	private final ProductService productService;

	@KafkaListener(topics = "product-price-changes", groupId = "demo")
	public void handle(ProductPriceChangedEvent event) {
		log.info("Received a ProductPriceChangedEvent with productCode:{}: ", event.getProductCode());
		productService.updateProductPrice(event.getProductCode(), event.getPrice());
	}

}