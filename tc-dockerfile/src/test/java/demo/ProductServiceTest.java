package demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;

@Testcontainers
public class ProductServiceTest {

	static DBProvider dbProvider;

	ProductService productService;

	@Container
	static GenericContainer postgres = new GenericContainer<>(
			new ImageFromDockerfile().withFileFromClasspath("Dockerfile", "Dockerfile")
				.withFileFromClasspath("sql/1-initdb.sql", "sql/1-initdb.sql"))
		.withEnv("POSTGRES_USER", "siva")
		.withEnv("POSTGRES_PASSWORD", "secret")
		.withEnv("POSTGRES_DB", "appdb")
		.withExposedPorts(5432);

	/*
	 * @Container static GenericContainer postgres = new GenericContainer<>(new
	 * ImageFromDockerfile() .withDockerfileFromBuilder(builder -> builder
	 * .from("postgres:15.2-alpine") .build())) .withClasspathResourceMapping("sql/",
	 * "/docker-entrypoint-initdb.d/", BindMode.READ_ONLY)
	 * .withEnv("POSTGRES_USER","siva") .withEnv("POSTGRES_PASSWORD","secret")
	 * .withEnv("POSTGRES_DB","appdb") .withExposedPorts(5432) ;
	 */

	@BeforeAll
	static void beforeAll() throws SQLException {
		dbProvider = new DBProvider("org.postgresql.Driver",
				"jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/appdb", "siva",
				"secret");
	}

	@BeforeEach
	void setUp() {
		productService = new ProductService(dbProvider);
	}

	@Test
	void shouldGetProducts() throws SQLException {
		List<Product> products = productService.getAll();
		Assertions.assertEquals(3, products.size());
	}

}