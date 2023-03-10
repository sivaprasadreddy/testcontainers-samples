package com.sivalabs.tcdemo.rest;

import com.sivalabs.tcdemo.domain.CreateProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sivalabs.tcdemo.config.RabbitMQConfig.routingKey;
import static com.sivalabs.tcdemo.config.RabbitMQConfig.topicExchangeName;

@RestController
@RequestMapping("/api/products/events")
@RequiredArgsConstructor
@Slf4j
public class ProductEventsController {

	private final RabbitTemplate rabbitTemplate;

	@PostMapping
	public void handleEvent(@RequestBody CreateProductRequest payload) {
		rabbitTemplate.convertAndSend(topicExchangeName, routingKey, payload);
		log.info("CreateProductRequest published to RabbitMQ");
	}

}
