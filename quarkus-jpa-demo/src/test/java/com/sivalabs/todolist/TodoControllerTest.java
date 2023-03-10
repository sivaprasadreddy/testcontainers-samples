package com.sivalabs.todolist;

import com.sivalabs.PostgresResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
// @QuarkusTestResource(PostgresResource.class)
class TodoControllerTest {

	@Test
	void allTodos() {
		List<Todo> todoList = given().when()
			.get("/api/todos")
			.then()
			.statusCode(200)
			.extract()
			.as(new TypeRef<List<Todo>>() {
			});
		Assertions.assertNotNull(todoList);
	}

	@Test
	void createTodo() {
		String body = """
				{
				"content" : "Learn ABCDEF",
				"done": false
				}
				""";
		given().contentType(ContentType.JSON)
			.body(body)

			.when()
			.post("/api/todos")
			.then()
			.statusCode(201);
	}

}