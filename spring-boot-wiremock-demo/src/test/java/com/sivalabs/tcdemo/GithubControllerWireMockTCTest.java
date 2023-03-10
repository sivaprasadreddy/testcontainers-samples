package com.sivalabs.tcdemo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@Disabled
public class GithubControllerWireMockTCTest {

	@Autowired
	protected MockMvc mockMvc;

	private static WireMockServer wireMockServer;

	static DockerImageName imageName = DockerImageName.parse("wiremock/wiremock:2.35.0-alpine");

	@Container
	static GenericContainer<?> wiremockContainer = new GenericContainer<>(imageName).withExposedPorts(8080);

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		String host = wiremockContainer.getHost();
		Integer mappedPort = wiremockContainer.getMappedPort(8080);
		String wireMockUrl = "http://" + host + ":" + mappedPort;
		System.out.println("wireMockUrl:" + wireMockUrl);
		wireMockServer = new WireMockServer(options().bindAddress(host).port(mappedPort));
		wireMockServer.start();
		System.out.println("baseUrl:" + wireMockServer.baseUrl());
		registry.add("github.api.base-url", wireMockServer::baseUrl);
	}

	@Test
	void shouldGetGithubUserProfile() throws Exception {
		String username = "sivaprasadreddy";
		mockGetUserFromGithub(username);
		this.mockMvc.perform(get("/api/users/{username}", username))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.login", is(username)))
			.andExpect(jsonPath("$.name", is("K. Siva Prasad Reddy")))
			.andExpect(jsonPath("$.public_repos", is(50)));
	}

	private void mockGetUserFromGithub(String username) {
		wireMockServer.stubFor(WireMock.get(urlMatching("/users/.*"))
			.willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody("""
					{
					"login": "%s",
					"name": "K. Siva Prasad Reddy",
					"twitter_username": "sivalabs",
					"public_repos": 50
					}
					""".formatted(username))));
	}

}