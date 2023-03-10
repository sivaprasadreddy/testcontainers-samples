package com.sivalabs.productservice.controller;

import com.sivalabs.productservice.domain.Product;
import com.sivalabs.productservice.domain.ProductRepository;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerTest {

	private static final String TOXIPROXY_NETWORK_ALIAS = "toxiproxy";

	static Network network = Network.newNetwork();

	private static final DockerImageName TOXIPROXY_IMAGE = DockerImageName.parse("shopify/toxiproxy");

	private static final DockerImageName WIREMOCK_IMAGE = DockerImageName.parse("wiremock/wiremock:2.33.2");

	@Container
	static ToxiproxyContainer toxiproxy = new ToxiproxyContainer(TOXIPROXY_IMAGE).withNetwork(network)
		.withNetworkAliases(TOXIPROXY_NETWORK_ALIAS);

	@Container
	static GenericContainer<?> wiremockContainer = new GenericContainer<>(WIREMOCK_IMAGE).withNetwork(network)
		.withExposedPorts(8080)
		.withClasspathResourceMapping("/wiremock", "/home/wiremock", BindMode.READ_ONLY)
		.waitingFor(Wait.forHttp("/__admin/mappings").withMethod("GET").forStatusCode(200));

	static ToxiproxyContainer.ContainerProxy proxy;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		proxy = toxiproxy.getProxy(wiremockContainer, 8080);
		String ipAddressViaToxiproxy = proxy.getContainerIpAddress();
		int portViaToxiproxy = proxy.getProxyPort();
		String proxyUrl = "http://" + ipAddressViaToxiproxy + ":" + portViaToxiproxy;
		System.out.println("proxyUrl:" + proxyUrl);
		registry.add("app.promotion-service-url", () -> proxyUrl);
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ProductRepository repository;

	@BeforeEach
	void setUp() {
		repository.deleteAll();
		Product p1 = new Product(1L, "Lenovo Laptop", new BigDecimal(65000));
		repository.save(p1);
	}

	@AfterEach
	void tearDown() throws IOException {
		Toxic latency = proxy.toxics()
			.getAll()
			.stream()
			.filter(toxic -> toxic.getName().equals("latency"))
			.findFirst()
			.orElse(null);
		if (latency != null) {
			latency.remove();
		}
	}

	@Test
	void shouldReturnAllProductsWithPromotions() throws Exception {
		mockMvc.perform(get("/api/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", Matchers.equalTo(1)))
			.andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
			.andExpect(jsonPath("$[0].name", Matchers.equalTo("Lenovo Laptop")))
			.andExpect(jsonPath("$[0].originalPrice", Matchers.equalTo(65000.0)))
			.andExpect(jsonPath("$[0].discount", Matchers.equalTo(3000)))
			.andExpect(jsonPath("$[0].price", Matchers.equalTo(62000.0)));
	}

	@Test
	void shouldReturnAllProductsWithoutPromotions() throws Exception {
		proxy.toxics().latency("latency", ToxicDirection.DOWNSTREAM, 10_100).setJitter(100);

		mockMvc.perform(get("/api/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", Matchers.equalTo(1)))
			.andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
			.andExpect(jsonPath("$[0].name", Matchers.equalTo("Lenovo Laptop")))
			.andExpect(jsonPath("$[0].originalPrice", Matchers.equalTo(65000.0)))
			.andExpect(jsonPath("$[0].discount", Matchers.equalTo(0)))
			.andExpect(jsonPath("$[0].price", Matchers.equalTo(65000.0)));
	}

}