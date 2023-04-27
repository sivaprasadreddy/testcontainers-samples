package com.sivalabs.todolist;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/api/todos")
public class TodoController {

	@GET
	public List<Todo> allTodos() {
		return Todo.listAll();
	}

	@POST
	@Transactional
	public Response createTodo(@Valid Todo todo) {
		Todo.persist(todo);
		return Response.created(URI.create("/api/todos/" + todo.id)).build();
	}

}
