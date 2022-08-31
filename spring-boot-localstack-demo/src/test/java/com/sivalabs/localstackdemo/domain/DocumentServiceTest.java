package com.sivalabs.localstackdemo.domain;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sivalabs.localstackdemo.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@SpringBootTest
@Testcontainers
class DocumentServiceTest {
    @Container
    static LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:1.0.4"))
            .withServices(S3)
            .withExposedPorts(4566);

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public AmazonS3 amazonS3LocalStack()  {
            return AmazonS3ClientBuilder.standard()
                    .enablePathStyleAccess()
                    .withEndpointConfiguration(endpointConfiguration(S3))
                    .withCredentials(credentialsProvider())
                    .build();
        }

        private AwsClientBuilder.EndpointConfiguration endpointConfiguration(LocalStackContainer.Service service) {
            return new AwsClientBuilder.EndpointConfiguration(
                    localStack.getEndpointOverride(service).toString(),
                    localStack.getRegion()
            );
        }

        private AWSCredentialsProvider credentialsProvider() {
            return new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(localStack.getAccessKey(),
                            localStack.getSecretKey()));
        }
    }

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ApplicationProperties properties;

    @BeforeEach
    void setUp() {
        amazonS3.createBucket(properties.bucketName());
    }

    @Test
    void shouldUpload() {
        InputStream is = new ByteArrayInputStream("sivalabs".getBytes(StandardCharsets.UTF_8));
        documentService.upload("first.txt", is);
        List<String> files = documentService.listFiles(properties.bucketName());
        assertThat(files).contains("first.txt");
    }
}