package com.sivalabs.tcdemo.domain;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.couchbase.DataCouchbaseTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataCouchbaseTest(
        properties = {"spring.couchbase.env.timeouts.connect=2m", "spring.data.couchbase.bucket-name=cbbucket"})
@Testcontainers
class ProductRepositoryTest {

    private static final String BUCKET_NAME = "cbbucket";

    @Container
    @ServiceConnection
    static final CouchbaseContainer couchbase = new CouchbaseContainer("couchbase/server:7.1.4")
            .withStartupAttempts(5)
            .withStartupTimeout(Duration.ofMinutes(10))
            .withBucket(new BucketDefinition(BUCKET_NAME));

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        productRepository.save(new Product(null, "pname1", "pdescr1", BigDecimal.TEN, false));
        productRepository.save(new Product(null, "pname2", "pdescr2", BigDecimal.TEN, true));
    }

    @Test
    void shouldGetAllProducts() {
        await().pollInterval(Duration.ofSeconds(3)).atMost(30, SECONDS).untilAsserted(() -> {
            List<Product> products = productRepository.findAll();
            assertThat(products).hasSize(2);
        });
    }
}
