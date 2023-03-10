package com.sivalabs.myapp;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.HttpStatus.OK;

@MicronautTest
class TodoControllerTest {

	private BlockingHttpClient blockingClient;

	@Inject
	@Client("/")
	HttpClient client;

	@BeforeEach
	void setup() {
		blockingClient = client.toBlocking();
	}

	@Test
	void shouldCreateTodo() {
		HttpRequest<?> request = HttpRequest.POST("/todos", new Todo(null, "DevOps", false));
		HttpResponse<?> response = blockingClient.exchange(request);

		assertEquals(CREATED, response.getStatus());
	}

	@Test
	void shouldGetAllTodos() {
		HttpRequest<?> request = HttpRequest.GET("/todos");
		HttpResponse<?> response = blockingClient.exchange(request);

		assertEquals(OK, response.getStatus());
	}

}