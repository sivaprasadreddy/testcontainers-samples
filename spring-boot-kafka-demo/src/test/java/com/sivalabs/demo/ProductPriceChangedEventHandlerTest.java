package com.sivalabs.demo;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @TestPropertySource(properties = {
// "spring.datasource.url=jdbc:tc:postgresql:17-alpine:///db?TC_INITSCRIPT=sql/schema.sql"
// })
@Testcontainers
@Slf4j
class ProductPriceChangedEventHandlerTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("sql/schema.sql"), "/docker-entrypoint-initdb.d/schema.sql");

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    /*
     * @DynamicPropertySource static void
     * overridePropertiesInternal(DynamicPropertyRegistry registry) {
     * registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers); }
     */

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        Product product = new Product(null, "P100", "Product One", BigDecimal.TEN);
        productRepository.save(product);
    }

    @Test
    void shouldHandleProductPriceChangedEvent() {
        ProductPriceChangedEvent event =
                new ProductPriceChangedEvent("P100", new BigDecimal("14.50"), LocalDateTime.now());

        log.info(
                "Publishing ProductPriceChangedEvent with ProductCode: {} at: {}",
                event.getProductCode(),
                event.getEventDate());
        kafkaTemplate.send("product-price-changes", event.getProductCode(), event);

        await().pollInterval(Duration.ofSeconds(3)).atMost(10, SECONDS).untilAsserted(() -> {
            Optional<Product> optionalProduct = productRepository.findByCode("P100");
            assertThat(optionalProduct).isPresent();
            assertThat(optionalProduct.get().getCode()).isEqualTo("P100");
            assertThat(optionalProduct.get().getPrice()).isEqualTo(new BigDecimal("14.50"));
        });
    }
}
