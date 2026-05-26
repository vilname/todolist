package com.example.todolist.controller;

import com.example.todolist.dto.TodoRequest;
import com.example.todolist.dto.TodoResponse;
import com.example.todolist.service.TodoService;
import com.example.todolist.util.exeption.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todo API", description = "Endpoints for managing TODO items")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    @Operation(summary = "Get all TODO items", description = "Returns a list of all TODO items")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get TODO item by ID", description = "Returns a single TODO item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved TODO item"),
            @ApiResponse(responseCode = "404", description = "TODO item not found")
    })
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new TODO item", description = "Creates a new TODO item")
    @ApiResponse(responseCode = "201", description = "TODO item created successfully")
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoRequest request) {
        TodoResponse response = todoService.createTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing TODO item", description = "Updates an existing TODO item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TODO item updated successfully"),
            @ApiResponse(responseCode = "404", description = "TODO item not found")
    })
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.updateTodo(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a TODO item", description = "Deletes a TODO item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "TODO item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "TODO item not found")
    })
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get TODO items by status", description = "Returns TODO items filtered by status")
    public ResponseEntity<List<TodoResponse>> getTodosByStatus(@PathVariable String status) {
        return ResponseEntity.ok(todoService.getTodosByStatus(status));
    }

    @GetMapping("/search")
    @Operation(summary = "Search TODO items by title", description = "Searches TODO items by title (case-insensitive)")
    public ResponseEntity<List<TodoResponse>> searchTodos(@RequestParam String title) {
        return ResponseEntity.ok(todoService.searchTodos(title));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}