package com.example.todolist;

import com.example.todolist.controller.TodoController;
import com.example.todolist.dto.TodoRequest;
import com.example.todolist.entity.Todo;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.util.enums.TodoStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldCreateTodo() throws Exception {
        TodoRequest request = new TodoRequest();
        request.setTitle("New Todo");
        request.setDescription("New Description");
        request.setStatus(TodoStatus.PENDING);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Todo"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(1);
        assertThat(todos.get(0).getTitle()).isEqualTo("New Todo");
    }

    @Test
    void shouldGetAllTodos() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todo.setStatus(TodoStatus.PENDING);
        todo.setCreatedAt(LocalDateTime.now());
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Todo"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void shouldGetTodoById() throws Exception {
        Todo savedTodo = todoRepository.save(createTestTodo());

        mockMvc.perform(get("/api/todos/{id}", savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentTodo() throws Exception {
        mockMvc.perform(get("/api/todos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        Todo savedTodo = todoRepository.save(createTestTodo());

        TodoRequest updateRequest = new TodoRequest();
        updateRequest.setTitle("Updated Todo");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStatus(TodoStatus.COMPLETED);

        mockMvc.perform(put("/api/todos/{id}", savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        Todo updatedTodo = todoRepository.findById(savedTodo.getId()).orElse(null);
        assertThat(updatedTodo).isNotNull();
        assertThat(updatedTodo.getTitle()).isEqualTo("Updated Todo");
        assertThat(updatedTodo.getStatus()).isEqualTo(TodoStatus.COMPLETED);
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        Todo savedTodo = todoRepository.save(createTestTodo());

        mockMvc.perform(delete("/api/todos/{id}", savedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(todoRepository.findById(savedTodo.getId())).isEmpty();
    }

    @Test
    void shouldGetTodosByStatus() throws Exception {
        Todo todo1 = new Todo();
        todo1.setTitle("Todo 1");
        todo1.setStatus(TodoStatus.PENDING);
        todo1.setCreatedAt(LocalDateTime.now());
        todoRepository.save(todo1);

        Todo todo2 = new Todo();
        todo2.setTitle("Todo 2");
        todo2.setStatus(TodoStatus.PENDING);
        todo2.setCreatedAt(LocalDateTime.now());
        todoRepository.save(todo2);

        mockMvc.perform(get("/api/todos/status/PENDING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").hasJsonPath());
    }

    @Test
    void shouldSearchTodosByTitle() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Learn Spring Boot");
        todo.setStatus(TodoStatus.PENDING);
        todo.setCreatedAt(LocalDateTime.now());
        todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/search")
                        .param("title", "spring")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").value(hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Learn Spring Boot"));
    }

    private Todo createTestTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todo.setStatus(TodoStatus.PENDING);
        todo.setCreatedAt(LocalDateTime.now());
        return todo;
    }
}