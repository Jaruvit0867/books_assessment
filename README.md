# Books API

A RESTful API for managing books, built with Spring Boot and MySQL.

## Prerequisites

- Java 25
- Maven
- MySQL 8+

## Database Setup

### 1. Start MySQL

```bash
brew services start mysql
```

### 2. Create database and user

```bash
mysql -u root
```

```sql
CREATE DATABASE books_db;
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'password1234';
GRANT ALL PRIVILEGES ON books_db.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Create test database (for integration tests)

```bash
mysql -u root -e "CREATE DATABASE IF NOT EXISTS books_db_test; GRANT ALL PRIVILEGES ON books_db_test.* TO 'admin'@'localhost'; FLUSH PRIVILEGES;"
```

Note: Tables are created automatically by Hibernate when the application starts. No manual schema creation needed.

## Run the Server

```bash
./mvnw spring-boot:run
```

Server runs on `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Create a book

```
POST /books
```

Request body:

```json
{
  "title": "Harry Potter",
  "author": "J.K. Rowling",
  "publishedDate": "30-06-2540"
}
```

Note: `publishedDate` uses **Buddhist calendar** (พ.ศ.) in `dd-MM-yyyy` format.

Response (201 Created):

```json
{
  "id": 1,
  "title": "Harry Potter",
  "author": "J.K. Rowling",
  "publishedDate": "1997-06-30"
}
```

### Get books by author

```
GET /books?author={authorName}
```

Example:

```bash
curl "http://localhost:8080/books?author=J.K.%20Rowling"
```

Response (200 OK):

```json
[
  {
    "id": 1,
    "title": "Harry Potter",
    "author": "J.K. Rowling",
    "publishedDate": "1997-06-30"
  },
  {
    "id": 2,
    "title": "Fantastic Beasts",
    "author": "J.K. Rowling",
    "publishedDate": "2001-11-15"
  }
]
```

Returns empty array if no books found.

## Validation

| Field | Rule |
|-------|------|
| `title` | Required, must not be empty |
| `author` | Required, must not be empty |
| `publishedDate` | Required, must be valid date in `dd-MM-yyyy` (Buddhist calendar), year must be > 1000 and <= current year (Gregorian) |

Example validation error (400 Bad Request):

```json
{
  "timestamp": "2026-06-05T09:00:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for object='bookRequest'. Error count: 1",
  "errors": [
    {
      "field": "title",
      "message": "title must not be empty"
    }
  ],
  "path": "/books"
}
```

## Run Integration Tests

```bash
./mvnw test
```

Tests run against `books_db_test` database (separate from the main database).

## Project Structure

```
src/main/java/com/myrepo/assessment/
├── AssessmentApplication.java
├── controller/
│   └── BookController.java
├── dto/
│   ├── BookRequest.java
│   └── BookResponse.java
├── entity/
│   └── Book.java
├── repository/
│   └── BookRepository.java
├── service/
│   └── BookService.java
└── validator/
    ├── BuddhistDateValidator.java
    └── ValidBuddhistDate.java
```
