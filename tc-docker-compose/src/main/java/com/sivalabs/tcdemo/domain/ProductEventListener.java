package com.sivalabs.tcdemo.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventListener {

	private final ProductService productService;

	@RabbitListener(queues = "products")
	public void handle(CreateProductRequest request) {
		Product product = new Product(null, request.getCode(), request.getName(), request.getPrice());
		productService.save(product);
	}

}
