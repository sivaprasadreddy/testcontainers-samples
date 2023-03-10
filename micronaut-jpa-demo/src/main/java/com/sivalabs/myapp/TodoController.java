package com.sivalabs.myapp;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/todos")
@RequiredArgsConstructor
public class TodoController {

	private final TodoRepository repo;

	@Get
	Iterable<Todo> list() {
		return repo.findAll();
	}

	@Post
	HttpResponse<Todo> save(@Body Todo todo) {
		Todo savedTodo = repo.save(todo);
		return HttpResponse.created(savedTodo);
	}

}
