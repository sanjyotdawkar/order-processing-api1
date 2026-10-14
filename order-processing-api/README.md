# Order Processing REST API (Spring Boot)

A simple backend for managing **Products** and placing **Orders**. No frontend.

## Tech
Java 17, Spring Boot 3.2, Spring Data JPA (Hibernate), MySQL, Bean Validation, springdoc-openapi (Swagger), JUnit 5 + Mockito + MockMvc (tests use H2).

## Run it
1. Install Java 17+, Maven and MySQL; start MySQL.
2. Open `src/main/resources/application.properties` and set your MySQL `username` / `password`.
   The database `order_db` and tables are created automatically (`schema.sql`).
3. Start the app:
   ```
   mvn spring-boot:run
   ```
4. Swagger UI: http://localhost:8080/swagger-ui.html

## Run tests (no MySQL needed)
```
mvn test
```

## Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/products | Create product |
| GET | /api/products | List products |
| GET | /api/products/{id} | Get one product |
| PUT | /api/products/{id} | Update product |
| POST | /api/orders | Place order (deducts stock) |
| GET | /api/orders | List orders |
| GET | /api/orders/{id} | Get one order |

### Example
```
POST /api/products
{ "name": "Laptop", "description": "14 inch", "price": 55000.00, "stock": 10 }

POST /api/orders
{ "productId": 1, "quantity": 2 }
```

## Business rules
- Quantity must be at least 1.
- Product must exist (else 404).
- Out of stock or quantity > stock -> 409 Conflict, nothing is saved.
- On success, stock is reduced and total price = price x quantity (all in one transaction).

## Error format
```
{ "timestamp": "...", "status": 400, "error": "Bad Request",
  "message": "Validation failed", "details": ["quantity: Quantity must be at least 1"] }
```

## Project structure
```
controller/  -> REST endpoints
service/     -> business logic (stock rules live in OrderService)
repository/  -> database access (Spring Data JPA)
entity/      -> Product, Order (DB tables)
dto/         -> request bodies with validation
exception/   -> custom exceptions + GlobalExceptionHandler
config/      -> Swagger config
```
