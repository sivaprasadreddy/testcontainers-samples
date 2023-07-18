package com.sivalabs.tcdemo.rest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        properties = {"spring.couchbase.env.timeouts.connect=2m", "spring.data.couchbase.bucket-name=cbbucket"})
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerIntegrationTest {

    private static final String BUCKET_NAME = "cbbucket";

    @Container
    @ServiceConnection
    static final CouchbaseContainer couchbase = new CouchbaseContainer("couchbase/server:7.1.4")
            .withStartupAttempts(5)
            .withStartupTimeout(Duration.ofMinutes(10))
            .withBucket(new BucketDefinition(BUCKET_NAME));

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnActiveProducts() throws Exception {
        mockMvc.perform(get("/api/products")).andExpect(status().isOk());
    }
}
