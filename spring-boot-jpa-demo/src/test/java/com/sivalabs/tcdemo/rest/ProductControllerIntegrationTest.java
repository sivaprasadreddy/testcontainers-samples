package com.sivalabs.tcdemo.rest;

import com.sivalabs.tcdemo.infra.PostgresDatabaseContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = { PostgresDatabaseContainerInitializer.class })
class ProductControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnActiveProducts() throws Exception {
		mockMvc.perform(get("/api/products")).andExpect(status().isOk());
	}

}