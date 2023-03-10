package com.sivalabs.todolist;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
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
