package com.sivalabs.localstackdemo.domain;

import com.sivalabs.localstackdemo.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
	static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:2.0"))
		.withServices(S3);

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		String awsEndpoint = "http://" + localStack.getHost() + ":" + localStack.getFirstMappedPort();
		registry.add("spring.cloud.aws.endpoint", () -> awsEndpoint);
		registry.add("spring.cloud.aws.region.static", () -> localStack.getRegion());
		registry.add("spring.cloud.aws.credentials.access-key", () -> localStack.getAccessKey());
		registry.add("spring.cloud.aws.credentials.secret-key", () -> localStack.getSecretKey());
		registry.add("spring.cloud.aws.s3.path-style-access-enabled", () -> true);
	}

	@Autowired
	private DocumentService documentService;

	@Autowired
	private AmazonS3Service amazonS3Service;

	@Autowired
	private ApplicationProperties properties;

	@BeforeEach
	void setUp() {
		amazonS3Service.createBucket(properties.bucketName());
	}

	@Test
	void shouldUpload() {
		InputStream is = new ByteArrayInputStream("sivalabs".getBytes(StandardCharsets.UTF_8));
		documentService.upload("first.txt", is);
		List<String> files = documentService.listFiles(properties.bucketName());
		assertThat(files).contains("first.txt");
	}

}