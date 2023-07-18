package com.sivalabs.myapp;

import static jakarta.persistence.GenerationType.IDENTITY;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "todos")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Serdeable
public class Todo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull @Column(name = "content", nullable = false)
    private String content;

    private boolean done;
}
