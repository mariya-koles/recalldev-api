# Recall.dev Backend

A Spring Boot REST API for managing quiz questions and flashcards for programming interview preparation.

## Features

- **Question Management**: Create, read, update, and delete programming interview questions
- **Tag System**: Organize questions with tags for easy categorization
- **Difficulty Levels**: Questions can be marked as EASY, MEDIUM, or HARD
- **Search Functionality**: Search questions by text content or tags
- **RESTful API**: Complete REST API with proper HTTP status codes
- **API Documentation**: Interactive Swagger UI for testing and documentation

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL** (with H2 for testing)
- **Maven**
- **Swagger/OpenAPI 3** for API documentation

## Database Schema

The application uses the following PostgreSQL tables:

### Questions Table
```sql
CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    question_text TEXT NOT NULL,
    question_answer TEXT NOT NULL,
    difficulty difficulty_level NOT NULL DEFAULT 'MEDIUM'
);
```

### Tags Table
```sql
CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);
```

### Question-Tags Junction Table
```sql
CREATE TABLE question_tags (
    question_id INT REFERENCES questions(id) ON DELETE CASCADE,
    tag_id INT REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (question_id, tag_id)
);
```

### Custom Enum Type
```sql
CREATE TYPE difficulty_level AS ENUM ('EASY', 'MEDIUM', 'HARD');
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Database Setup
1. Create a PostgreSQL database named `recall-dev`
2. Run the DDL scripts provided above to create the tables
3. Update the database connection details in `src/main/resources/application.yml`

### Environment Variables
Set the following environment variables (optional):
- `DB_USERNAME`: PostgreSQL username (default: postgres)
- `DB_PASSWORD`: PostgreSQL password (default: password)

### Running the Application
```bash
# Clone the repository
git clone <repository-url>
cd recall.dev

# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation & Testing

### Swagger UI
Once the application is running, you can access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

The Swagger UI provides:
- Complete API documentation with request/response examples
- Interactive testing interface for all endpoints
- Schema definitions for all DTOs and entities
- Parameter descriptions and validation rules

### Quick Testing Guide

1. **Start the application** and navigate to http://localhost:8080/swagger-ui.html
2. **Create a tag** first using `POST /api/tags`:
   ```json
   {
     "name": "algorithms"
   }
   ```
3. **Create a question** using `POST /api/questions`:
   ```json
   {
     "questionText": "What is the time complexity of binary search?",
     "questionAnswer": "O(log n) - Binary search eliminates half of the search space in each iteration.",
     "difficulty": "MEDIUM"
   }
   ```
4. **Add the tag to the question** using `POST /api/questions/{id}/tags/algorithms`
5. **Test other endpoints** like search, filtering by difficulty, etc.

## API Endpoints

### Questions API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/questions` | Get all questions |
| GET | `/api/questions?includeTags=true` | Get all questions with tags |
| GET | `/api/questions/{id}` | Get question by ID |
| GET | `/api/questions/{id}?includeTags=true` | Get question by ID with tags |
| POST | `/api/questions` | Create new question |
| PUT | `/api/questions/{id}` | Update question |
| DELETE | `/api/questions/{id}` | Delete question |
| GET | `/api/questions/difficulty/{difficulty}` | Get questions by difficulty |
| GET | `/api/questions/search?keyword={keyword}` | Search questions |
| GET | `/api/questions/tag/{tagName}` | Get questions by tag |
| GET | `/api/questions/tags?tagNames={tag1,tag2}` | Get questions by multiple tags |
| POST | `/api/questions/{id}/tags/{tagName}` | Add tag to question |
| DELETE | `/api/questions/{id}/tags/{tagName}` | Remove tag from question |
| PUT | `/api/questions/{id}/tags` | Set question tags |

### Tags API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tags` | Get all tags |
| GET | `/api/tags?includeQuestions=true` | Get all tags with questions |
| GET | `/api/tags/{id}` | Get tag by ID |
| GET | `/api/tags/name/{name}` | Get tag by name |
| POST | `/api/tags` | Create new tag |
| PUT | `/api/tags/{id}` | Update tag |
| DELETE | `/api/tags/{id}` | Delete tag |
| GET | `/api/tags/search?keyword={keyword}` | Search tags |
| GET | `/api/tags/with-questions` | Get tags that have questions |
| GET | `/api/tags/without-questions` | Get tags without questions |

## Request/Response Examples

### Create Question
```json
POST /api/questions
{
    "questionText": "What is the time complexity of binary search?",
    "questionAnswer": "O(log n) - Binary search eliminates half of the search space in each iteration.",
    "difficulty": "MEDIUM"
}
```

### Create Tag
```json
POST /api/tags
{
    "name": "algorithms"
}
```

### Add Tag to Question
```json
POST /api/questions/1/tags/algorithms
```

## Project Structure

```
src/main/java/com/interview/quizflashcards/
├── QuizFlashcardsApplication.java          # Main application class
├── config/
│   └── OpenApiConfig.java                  # Swagger configuration
├── entity/
│   ├── Question.java                       # Question entity
│   └── Tag.java                           # Tag entity
├── repository/
│   ├── QuestionRepository.java            # Question data access
│   └── TagRepository.java                 # Tag data access
├── service/
│   ├── QuestionService.java               # Question business logic
│   └── TagService.java                    # Tag business logic
└── controller/
    ├── QuestionController.java            # Question REST endpoints
    └── TagController.java                 # Tag REST endpoints
```

## Development

### Building
```bash
mvn clean compile
```

### Testing
```bash
mvn test
```

### Packaging
```bash
mvn clean package
```

## Configuration

The application can be configured via `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/recall-dev
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: validate  # Change to 'create-drop' for development

# Swagger Configuration
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License. 