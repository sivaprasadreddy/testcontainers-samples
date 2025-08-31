package com.sivalabs.localstackdemo;

import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
    public static final String BUCKET_NAME = UUID.randomUUID().toString();
    public static final String QUEUE_NAME = UUID.randomUUID().toString();

    @Bean
    @ServiceConnection
    LocalStackContainer localstackContainer() {
        LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:4.7.0"));
        try {
            localStack.start();
            localStack.execInContainer("awslocal", "s3", "mb", "s3://" + BUCKET_NAME);
            localStack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", QUEUE_NAME);
            Container.ExecResult execResult = localStack.execInContainer(
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
        return localStack;
    }

    @Bean
    DynamicPropertyRegistrar dynamicPropertyRegistrar() {
        return (registry) -> {
            registry.add("app.bucket", () -> BUCKET_NAME);
            registry.add("app.queue", () -> QUEUE_NAME);
        };
    }
}
