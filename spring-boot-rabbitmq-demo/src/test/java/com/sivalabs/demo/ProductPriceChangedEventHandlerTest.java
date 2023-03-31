package com.sivalabs.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Optional;

import static com.sivalabs.demo.RabbitMQConfig.EXCHANGE_NAME;
import static com.sivalabs.demo.RabbitMQConfig.ROUTING_KEY;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
		properties = { "spring.datasource.url=jdbc:tc:postgresql:15.2-alpine:///db?TC_INITSCRIPT=sql/schema.sql" })
@Testcontainers
@Slf4j
class ProductPriceChangedEventHandlerTest {

	@Container
	static final RabbitMQContainer rabbitmq = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.11.11-alpine"));

	@DynamicPropertySource
	static void overridePropertiesInternal(DynamicPropertyRegistry registry) {
		registry.add("spring.rabbitmq.host", rabbitmq::getHost);
		registry.add("spring.rabbitmq.port", rabbitmq::getAmqpPort);
		registry.add("spring.rabbitmq.username", rabbitmq::getAdminUsername);
		registry.add("spring.rabbitmq.password", rabbitmq::getAdminPassword);
	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		Product product = new Product(null, "P100", "Product One", BigDecimal.TEN);
		productRepository.save(product);
	}

	@Test
	void shouldHandleProductPriceChangedEvent() {
		ProductPriceChangedEvent event = new ProductPriceChangedEvent("P100", new BigDecimal("14.50"));

		log.info("Publishing ProductPriceChangedEvent with ProductCode: {}", event.getProductCode());
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event);

		await().atMost(10, SECONDS).untilAsserted(() -> {
			Optional<Product> optionalProduct = productRepository.findByCode("P100");
			assertThat(optionalProduct).isPresent();
			assertThat(optionalProduct.get().getCode()).isEqualTo("P100");
			assertThat(optionalProduct.get().getPrice()).isEqualTo(new BigDecimal("14.50"));
		});
	}

}