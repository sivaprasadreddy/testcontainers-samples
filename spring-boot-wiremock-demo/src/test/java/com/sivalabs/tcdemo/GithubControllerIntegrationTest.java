package com.sivalabs.tcdemo;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GithubControllerIntegrationTest {

	@Autowired
	protected MockMvc mockMvc;

	@RegisterExtension
	static WireMockExtension wireMockServer = WireMockExtension.newInstance()
		.options(wireMockConfig().dynamicPort())
		.build();

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
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