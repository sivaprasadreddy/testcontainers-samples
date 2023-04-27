package com.sivalabs.todolist;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class Todo extends PanacheEntity {

	@NotBlank(message = "content may not be blank")
	public String content;

	public boolean done;

	public static List<Todo> findCompleted() {
		return list("done", true);
	}

}
