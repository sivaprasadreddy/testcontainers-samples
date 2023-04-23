package com.sivalabs.mongodbdemo.api;

import com.sivalabs.mongodbdemo.domain.Product;
import com.sivalabs.mongodbdemo.domain.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerTest {

	@Container
	@ServiceConnection
	static MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:6.0.5"));

	/*
	 * @DynamicPropertySource static void configureProperties(DynamicPropertyRegistry
	 * registry) { registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl); }
	 */

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService.deleteAll();
		List<Product> products = List.of(new Product(null, "product-1 name", "product-1 desc", new BigDecimal("24.50")),
				new Product(null, "product-2 name", "product-2 desc", new BigDecimal("54.50")));
		products.forEach(productService::saveProduct);
	}

	@Test
	void shouldReturnAllProducts() throws Exception {
		mockMvc.perform(get("/api/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", Matchers.equalTo(2)));
	}

	@Test
	void shouldSaveProduct() throws Exception {
		mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content("""
				{
				    "name": "test product",
				    "description": "test product description",
				    "price": 24.50
				}
				"""))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", Matchers.notNullValue()))
			.andExpect(jsonPath("$.name", Matchers.equalTo("test product")))
			.andExpect(jsonPath("$.description", Matchers.equalTo("test product description")))
			.andExpect(jsonPath("$.price", Matchers.equalTo(24.5)));
	}

}