package com.sivalabs.localstackdemo;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

import java.util.UUID;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
abstract class AbstractIntegrationTest {

    static LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.0.0"));

    static final String BUCKET_NAME = UUID.randomUUID().toString();
    static final String QUEUE_NAME = UUID.randomUUID().toString();

    static {
        Startables.deepStart(localStack).join();
        try {
            localStack.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
            localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", QUEUE_NAME);

            ExecResult execResult = localStack.execInContainer(
                    "awslocal",
                    "dynamodb",
                    "create-table",
                    "--table-name",
                    "person",
                    "--key-schema",
                    "AttributeName=id,KeyType=HASH",
                    "AttributeName=name,KeyType=RANGE",
                    "--attribute-definitions",
                    "AttributeName=id,AttributeType=S",
                    "AttributeName=name,AttributeType=S",
                    "--provisioned-throughput",
                    "ReadCapacityUnits=5,WriteCapacityUnits=5");

            System.out.println("execResult is : " + execResult.getStdout());
            System.out.println("execResult error is : " + execResult.getStderr());
            System.out.println("execResult exit code is : " + execResult.getExitCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("app.bucket", () -> BUCKET_NAME);
        registry.add("app.queue", () -> QUEUE_NAME);
        registry.add("spring.cloud.aws.region.static", () -> localStack.getRegion());
        registry.add("spring.cloud.aws.credentials.access-key", () -> localStack.getAccessKey());
        registry.add("spring.cloud.aws.credentials.secret-key", () -> localStack.getSecretKey());
        /*registry.add(
        "spring.cloud.aws.endpoint",
        () -> localStack.getEndpoint().toString());*/
        registry.add(
                "spring.cloud.aws.s3.endpoint",
                () -> localStack.getEndpointOverride(S3).toString());
        registry.add(
                "spring.cloud.aws.sqs.endpoint",
                () -> localStack.getEndpointOverride(SQS).toString());
        registry.add(
                "spring.cloud.aws.dynamodb.endpoint",
                () -> localStack.getEndpointOverride(DYNAMODB).toString());
    }
}
