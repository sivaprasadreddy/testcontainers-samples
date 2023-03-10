package com.sivalabs.tcdemo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
public class WireMockContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
		/*
		 *
		 * WireMockServer wireMockServer = new WireMockServer(new
		 * WireMockConfiguration().dynamicPort()); wireMockServer.start(); String
		 * wireMockUrl = wireMockServer.baseUrl();
		 */

		var imageName = DockerImageName.parse("wiremock/wiremock:2.35.0-alpine");
		var wiremockContainer = new GenericContainer(imageName).withExposedPorts(8080)
			.withClasspathResourceMapping("/wiremock", "/home/wiremock", BindMode.READ_ONLY)
			.waitingFor(Wait.forHttp("/__admin/mappings").withMethod("GET").forStatusCode(200));
		wiremockContainer.start();
		String host = wiremockContainer.getHost();
		Integer mappedPort = wiremockContainer.getMappedPort(8080);
		String wireMockUrl = "http://" + host + ":" + mappedPort;
		WireMockServer wireMockServer = new WireMockServer(options().bindAddress(host).port(mappedPort));

		configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);

		configurableApplicationContext.addApplicationListener(applicationEvent -> {
			if (applicationEvent instanceof ContextClosedEvent) {
				wireMockServer.stop();
			}
		});

		log.info("Setting Github API BaseUrl:" + wireMockUrl);
		TestPropertyValues.of("github.api.base-url=" + wireMockUrl)
			.applyTo(configurableApplicationContext.getEnvironment());
	}

}
