package com.sivalabs.tcdemo.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ProductRepositoryWithServiceConnectionTest {

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
			DockerImageName.parse("postgres:15.2-alpine"));

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void shouldGetAllActiveProducts() {
		entityManager.persist(new Product(null, "pname1", "pdescr1", BigDecimal.TEN, false));
		entityManager.persist(new Product(null, "pname2", "pdescr2", BigDecimal.TEN, true));

		List<Product> products = productRepository.findAllActiveProducts();

		assertThat(products).hasSize(1);
		assertThat(products.get(0).getName()).isEqualTo("pname1");
	}

}