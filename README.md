# Order Processing API

## Overview

The Order Processing API is a RESTful backend application developed using Java and Spring Boot. It allows users to manage products and place orders through REST API endpoints.

The application uses Spring Data JPA to interact with a relational database and includes input validation and Swagger API documentation.

## Features

- Create new products
- Retrieve all products
- Retrieve a product by ID
- Update product details
- Place orders for existing products
- Retrieve all orders
- Retrieve an order by ID
- Validate request data
- API documentation using Swagger UI

## Technologies Used

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- Bean Validation
- Swagger / OpenAPI
- Postman for API testing

## Prerequisites

Before running the project, install:

- Java 17 or a compatible JDK
- IntelliJ IDEA or another Java IDE
- MySQL Server
- Maven (or use the Maven wrapper if included)

## Database Configuration

1. Start your MySQL server.
2. Create a database:

```sql
CREATE DATABASE order_processing_db;
```

3. Configure your database connection in `src/main/resources/application.properties`.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/order_processing_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Replace `YOUR_DB_USERNAME` and `YOUR_DB_PASSWORD` with your local MySQL credentials. Do not upload actual passwords to GitHub.

The application must have the MySQL JDBC driver dependency configured in `pom.xml`.

## How to Run the Project

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Configure the database connection in `application.properties`.
4. Allow IntelliJ to load the Maven dependencies.
5. Ensure the project SDK is configured correctly.
6. Run the Spring Boot main application class.

The backend will start at:

`http://localhost:8080`

## API Endpoints

### Product APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/products` | Create a product |
| GET | `/api/products` | Retrieve all products |
| GET | `/api/products/{id}` | Retrieve a product by ID |
| PUT | `/api/products/{id}` | Update a product |

### Order APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Place an order |
| GET | `/api/orders` | Retrieve all orders |
| GET | `/api/orders/{id}` | Retrieve an order by ID |

## Example Requests

### Create a Product

**POST** `/api/products`

```json
{
  "name": "Keyboard",
  "description": "USB keyboard",
  "price": 1000.00,
  "stock": 10
}
```

### Place an Order

**POST** `/api/orders`

```json
{
  "productId": 1,
  "quantity": 2
}
```

Use a valid product ID and ensure sufficient stock is available.

## Swagger API Documentation

If Swagger is enabled, access the API documentation at:

`http://localhost:8080/swagger-ui/index.html`

You can use Swagger UI to explore and test the available endpoints.

## Testing

Use Postman or Swagger UI to test the API endpoints.

Recommended test cases:

- Create a product with valid details.
- Retrieve all products.
- Place an order for an existing product.
- Retrieve the created order.
- Submit invalid input and verify validation errors.
- Test ordering a quantity greater than available stock.

## Frontend

This submission contains the backend REST API only. A separate frontend application is not included.

## Database Schema

The application uses JPA entities to map application data to database tables. The database schema is managed by Hibernate when the configured schema-generation setting is enabled.

A separate SQL schema or database dump can be added to the `database/` directory if required by the submission instructions.

**Database note:** This version is configured for MySQL. If PostgreSQL is required for submission, update the database dependency and connection settings accordingly.

## Author

Sanjyot Dawkar
