# Приложение "Список дел"

RESTful API на основе Spring Boot для управления задачами с полным набором операций CRUD, возможностью поиска и фильтрации.

## Содержание
- [Обзор проекта](#обзор-проекта)
- [Используемые технологии](#используемые-технологии)
- [Функции](#функции)
- [API Эндпоинты](#api-эндпоинты)
- [Начало работы](#начало-работы)
- [Запуск приложения](#запуск-приложения)
- [Конфигурация базы данных](#конфигурация-базы-данных)
- [Тестирование](#тестирование)
- [Документация API](#документация-api)
- [Структура проекта](#структура-проекта)

## Обзор проекта

Это RESTful API для управления задачами, построенное на Spring Boot. Приложение предоставляет полный набор операций CRUD для управления задачами с дополнительными функциями, такими как фильтрация по статусу и поиск по названию.

## Используемые технологии

- **Java 17** - Язык программирования
- **Spring Boot 3.4.5** - Фреймворк
- **Spring Web** - Разработка REST API
- **Spring Data JPA** - Доступ к данным и ORM
- **PostgreSQL** - База данных (зависимость времени выполнения)
- **Flyway** - Миграция базы данных
- **SpringDoc OpenAPI** - Документация API
- **Gradle** - Система сборки

## Функции

- Создание, просмотр, обновление и удаление задач
- Фильтрация задач по статусу (ОЖИДАНИЕ, В_ПРОЦЕССЕ, ЗАВЕРШЕНО)
- Поиск задач по названию (без учета регистра)
- Автоматическая отметка времени создания и обновления
- Валидация ввода
- Комплексная обработка ошибок
- Документация OpenAPI/Swagger
- Миграция базы данных с помощью Flyway

## API Эндпоинты

### Задачи

| Метод | Эндпоинт | Описание |
|--------|---------|-------------|
| GET | `/api/todos` | Получить все задачи |
| GET | `/api/todos/{id}` | Получить конкретную задачу по ID |
| POST | `/api/todos` | Создать новую задачу |
| PUT | `/api/todos/{id}` | Обновить существующую задачу |
| DELETE | `/api/todos/{id}` | Удалить задачу |
| GET | `/api/todos/status/{status}` | Получить задачи по статусу |
| GET | `/api/todos/search?title={query}` | Поиск задач по названию |

### Значения статуса

- `PENDING` (ОЖИДАНИЕ)
- `IN_PROGRESS` (В_ПРОЦЕССЕ)
- `COMPLETED` (ЗАВЕРШЕНО)

### Модель запроса/ответа

**Тело запроса (POST/PUT):**
```json
{
  "title": "строка (обязательно)",
  "description": "строка (опционально)",
  "status": "PENDING|IN_PROGRESS|COMPLETED"
}
```

**Тело ответа:**
```json
{
  "id": 1,
  "title": "строка",
  "description": "строка",
  "status": "PENDING|IN_PROGRESS|COMPLETED",
  "createdAt": "2023-01-01T10:00:00",
  "updatedAt": "2023-01-01T10:00:00"
}
```

## Начало работы

### Предварительные требования

- Java 17 или выше
- База данных PostgreSQL
- Gradle (опционально, в комплекте есть wrapper)

### Клонирование репозитория

```bash
git clone https://github.com/your-username/todolist.git
cd todolist
```

## Запуск приложения

### Использование Gradle Wrapper

```bash
# Сборка проекта
./gradlew build

# Запуск приложения
./gradlew bootRun
```

### Использование Docker

```bash
# Сборка образа Docker
docker build -t todolist .

# Запуск контейнера
docker run -p 8080:8080 todolist
```

Приложение будет доступно по адресу `http://localhost:8080`.

## Конфигурация базы данных

Приложение использует PostgreSQL в качестве базы данных. Конфигурация находится в файле `src/main/resources/application.yaml`.

По умолчанию приложение ожидает базу данных PostgreSQL со следующими параметрами:

- Имя базы данных: `todolist`
- Имя пользователя: `postgres`
- Пароль: `postgres`
- Хост: `localhost`
- Порт: `5432`

Вы можете изменить эти настройки в файле `application.yaml`.

## Тестирование

Запуск модульных и интеграционных тестов:

```bash
./gradlew test
```

## Документация API

API документировано с использованием OpenAPI (Swagger). После запуска приложения вы можете получить доступ к интерактивной документации API по адресу:

```
http://localhost:8080/swagger-ui.html
```

## Структура проекта

```
todolist/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── todolist/
│   │   │               ├── TodolistApplication.java
│   │   │               ├── controller/
│   │   │               │   └── TodoController.java
│   │   │               ├── service/
│   │   │               │   └── TodoService.java
│   │   │               ├── repository/
│   │   │               │   └── TodoRepository.java
│   │   │               ├── entity/
│   │   │               │   └── Todo.java
│   │   │               ├── dto/
│   │   │               │   ├── TodoRequest.java
│   │   │               │   └── TodoResponse.java
│   │   │               └── util/
│   │   │                   └── enums/
│   │   │                       └── TodoStatus.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-test.yaml
│   │       └── db/
│   │           └── migration/
│   │               └── V1__Create_todo_table.sql
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── todolist/
│                       ├── TodoControllerIntegrationTest.java
│                       └── TodoRepositoryIntegrationTest.java
├── build.gradle
├── settings.gradle
├── HELP.md
└── README.md
```

## Лицензия

Данный проект распространяется по лицензии MIT - подробности см. в файле [LICENSE](LICENSE).