package com.sivalabs.tcdemo.infra;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

public class MockServerContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static MockServerContainer mockServerContainer =
            new MockServerContainer(DockerImageName.parse("jamesdbloom/mockserver:mockserver-5.13.2"));

    static {
        mockServerContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of("github.api.base-url=" + mockServerContainer.getEndpoint()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}
