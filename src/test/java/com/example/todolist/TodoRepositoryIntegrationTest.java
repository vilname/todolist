package com.example.todolist;

import com.example.todolist.entity.Todo;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.util.enums.TodoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TodoRepositoryIntegrationTest {

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindTodoById() {
        Todo todo = new Todo();
        todo.setTitle("Test todo");
        todo.setDescription("Test description");
        todo.setStatus(TodoStatus.PENDING);
        todo.setCreatedAt(LocalDateTime.now());

        Todo savedTodo = todoRepository.save(todo);
        Todo foundTodo = todoRepository.findById(savedTodo.getId()).orElse(null);

        assertThat(foundTodo).isNotNull();
        assertThat(foundTodo.getTitle()).isEqualTo("Test todo");
        assertThat(foundTodo.getStatus()).isEqualTo(TodoStatus.PENDING);
    }

    @Test
    void shouldFindTodosByStatus() {
        Todo todo1 = new Todo();
        todo1.setTitle("Todo 1");
        todo1.setStatus(TodoStatus.PENDING);
        todo1.setCreatedAt(LocalDateTime.now());

        Todo todo2 = new Todo();
        todo2.setTitle("Todo 2");
        todo2.setStatus(TodoStatus.PENDING);
        todo2.setCreatedAt(LocalDateTime.now());

        Todo todo3 = new Todo();
        todo3.setTitle("Todo 3");
        todo3.setStatus(TodoStatus.COMPLETED);
        todo3.setCreatedAt(LocalDateTime.now());

        todoRepository.saveAll(List.of(todo1, todo2, todo3));

        List<Todo> pendingTodos = todoRepository.findByStatus(TodoStatus.PENDING);

        assertThat(pendingTodos).hasSize(2);
        assertThat(pendingTodos)
                .extracting(Todo::getStatus)
                .containsOnly(TodoStatus.PENDING);
    }

    @Test
    void shouldFindTodosByTitleContainingIgnoreCase() {
        Todo todo1 = new Todo();
        todo1.setTitle("Learn Spring Boot");
        todo1.setCreatedAt(LocalDateTime.now());
        todo1.setStatus(TodoStatus.PENDING);

        Todo todo2 = new Todo();
        todo2.setTitle("Learn React");
        todo2.setCreatedAt(LocalDateTime.now());
        todo2.setStatus(TodoStatus.PENDING);

        Todo todo3 = new Todo();
        todo3.setTitle("Build API");
        todo3.setCreatedAt(LocalDateTime.now());
        todo3.setStatus(TodoStatus.COMPLETED);

        todoRepository.saveAll(List.of(todo1, todo2, todo3));

        List<Todo> foundTodos = todoRepository.findByTitleContainingIgnoreCase("spring");

        assertThat(foundTodos).hasSize(1);
        assertThat(foundTodos.get(0).getTitle()).isEqualTo("Learn Spring Boot");
    }
}