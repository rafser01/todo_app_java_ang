package com.example.todo.controllers;

import javax.validation.Valid;
import com.example.todo.models.Todo;
import com.example.todo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TodoController {

	@Autowired
	TodoRepository todoRepository;

	@GetMapping("/todos")
	public List<Todo> getAllTodos() {
		Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
		return todoRepository.findAll(sortByCreatedAtDesc);
	}

	@PostMapping("/todos")
	public Todo createTodo(@Valid @RequestBody Todo todo) {
            System.out.println("Todo Post request "+todo.getTitle());
		todo.setCompleted(false);
		return todoRepository.save(todo);
	}

	@GetMapping(value = "/todos/{id}")
	public ResponseEntity<Todo> getTodoById(@PathVariable("id") String id) {
		return todoRepository.findById(id).map(todo -> ResponseEntity.ok().body(todo))
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping(value = "/todos/{id}")
	public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id, @Valid @RequestBody Todo todo) {
		return todoRepository.findById(id).map(todoData -> {
			todoData.setTitle(todo.getTitle());
			todoData.setCompleted(todo.getCompleted());
			Todo updatedTodo = todoRepository.save(todoData);
			return ResponseEntity.ok().body(updatedTodo);
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(value = "/todos/{id}")
	public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
		return todoRepository.findById(id).map(todo -> {
			todoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

}
