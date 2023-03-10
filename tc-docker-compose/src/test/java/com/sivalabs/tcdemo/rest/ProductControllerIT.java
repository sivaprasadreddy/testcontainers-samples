package com.sivalabs.tcdemo.rest;

import com.sivalabs.tcdemo.domain.Product;
import com.sivalabs.tcdemo.domain.ProductRepository;
import com.sivalabs.tcdemo.domain.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerIT {

	@Container
	static DockerComposeContainer compose = new DockerComposeContainer(
			new File("src/test/resources/docker-compose-test.yml"))
		.withExposedService("rabbitmq", 5672)
		.withExposedService("postgres", 5432);

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		String postgresHost = compose.getServiceHost("postgres", 5432);
		int postgresPort = compose.getServicePort("postgres", 5432);
		String jdbcUrl = "jdbc:postgresql://" + postgresHost + ":" + postgresPort + "/appdb";

		String rabbitmqHost = compose.getServiceHost("rabbitmq", 5672);
		int rabbitmqPort = compose.getServicePort("rabbitmq", 5672);

		registry.add("spring.datasource.url", () -> jdbcUrl);
		registry.add("spring.datasource.username", () -> "siva");
		registry.add("spring.datasource.password", () -> "secret");

		registry.add("spring.rabbitmq.host", () -> rabbitmqHost);
		registry.add("spring.rabbitmq.port", () -> rabbitmqPort);
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
	}

	@Test
	void shouldSaveProduct() throws Exception {
		mockMvc.perform(post("/api/products/events").contentType(MediaType.APPLICATION_JSON).content("""
				    {
				        "code": "p001",
				        "name": "product-001",
				        "price": 124.0
				    }
				""")).andExpect(status().isOk());

		await().atMost(5, SECONDS).until(() -> {
			Optional<Product> product = productService.getByCode("p001");
			return product.isPresent();
		});

	}

}