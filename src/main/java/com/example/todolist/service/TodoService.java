package com.example.todolist.service;

import com.example.todolist.dto.TodoRequest;
import com.example.todolist.dto.TodoResponse;
import com.example.todolist.entity.Todo;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.util.enums.TodoStatus;
import com.example.todolist.util.exeption.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<TodoResponse> getAllTodos() {
        return todoRepository.findAll()
                .stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }

    public TodoResponse getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return new TodoResponse(todo);
    }

    public TodoResponse createTodo(TodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setStatus(request.getStatus());

        Todo savedTodo = todoRepository.save(todo);
        return new TodoResponse(savedTodo);
    }

    public TodoResponse updateTodo(Long id, TodoRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setStatus(request.getStatus());

        Todo updatedTodo = todoRepository.save(todo);
        return new TodoResponse(updatedTodo);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        todoRepository.delete(todo);
    }

    public List<TodoResponse> getTodosByStatus(String status) {
        try {
            TodoStatus todoStatus = TodoStatus.valueOf(status.toUpperCase());
            return todoRepository.findByStatus(todoStatus)
                    .stream()
                    .map(TodoResponse::new)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Use: PENDING, IN_PROGRESS, or COMPLETED");
        }
    }

    public List<TodoResponse> searchTodos(String title) {
        return todoRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(TodoResponse::new)
                .collect(Collectors.toList());
    }
}
