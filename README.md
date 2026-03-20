# 🍽️ JavaEats

## 📌 Overview

**JavaEats** is a shared restaurant management platform designed to reduce operational costs and standardize processes across multiple establishments.

The system exposes a REST API that allows:

* Restaurants to manage menus, orders, and operations
* Clients to browse, order, and interact with restaurants

The application was built following **Clean Architecture principles**, ensuring:

* Separation of concerns
* Maintainability
* Scalability

---

## 🏗️ Architecture

The system adopts **Clean Architecture + Ports & Adapters (Hexagonal Architecture)**.

### 📐 Layers

* **Core**

  * Domain (entities, value objects)
  * Use Cases:

    * `in` → input ports (contracts)
    * `impl` → business logic
    * `out` → output ports (gateways)

* **Infrastructure (Infra)**

  * Web (REST Controllers + DTOs)
  * Persistence (MongoDB)
  * External integrations (Payment)
  * Security (JWT)

This design ensures that **business rules are independent of frameworks and infrastructure**.

---

## 🧱 Architectural Decisions

### 🗄️ Database: MongoDB

MongoDB was chosen due to:

* Flexible document structure
* Better modeling for aggregates (e.g., Orders with items)
* Reduced complexity (no joins)
* Horizontal scalability

---

### 🔍 CQRS (Command Query Responsibility Segregation)

The system partially applies CQRS:

* **Commands (write operations)**

  * Create/update entities

* **Queries (read operations)**

  * Optimized data retrieval

Example:

* `/user-addresses` → write
* `/queries/users/{userId}/addresses` → read

---

### ⚡ Event-Driven (Future Evolution)

The system is designed to support event-driven architecture.

Possible events:

* `OrderCreatedEvent`
* `PaymentProcessedEvent`

Benefits:

* Loose coupling
* Scalability
* Easier feature extension (notifications, logs, etc.)

---

## 🔄 Main Flow

### 🛒 Order Creation

1. Client sends request → `POST /orders`
2. Controller receives request
3. DTO is mapped to domain
4. Use Case processes business rules
5. Data is persisted via Gateway
6. MongoDB stores the document

---

### 💳 Payment Processing

1. Client sends request → `POST /orders/{orderId}/payments`
2. Payment Use Case is executed
3. External payment service is called
4. Order status is updated

---

## 🛡️ Resilience

The architecture includes several resilience points:

* Decoupling through interfaces (Gateways)
* Isolation of infrastructure
* External integrations via adapters
* Exception handling
* Prepared for retry / circuit breaker patterns
* Scalable NoSQL database (MongoDB)

---

## 🗂️ Project Structure

```bash
core/
 ├── domain/
 ├── usecase/
 │    ├── in
 │    ├── out
 │    └── impl

infra/
 ├── web/
 ├── persistence/
 ├── external_payment/
 ├── security/
 └── mapper/
```

---

## 📊 Architecture Documentation

Detailed architecture documentation (C4 Model + diagrams + flows) is available here:

👉 `docs/architecture.md`
👉 `docs/c4/`
👉 `docs/diagrams/`

The documentation includes:

* Context Diagram
* Container Diagram
* Component Diagram
* Code Structure
* Sequence Diagrams

---

## 🚀 Technology Stack

* Language: Java 21
* Framework: Spring Boot
* Data Persistence: Spring Data MongoDB
* Database: MongoDB
* Build Tool: Maven
* Containerization: Docker / Docker Compose
* Observability: Spring Actuator
* API Documentation: Swagger (OpenAPI)

---

## 📦 Requirements

Before running the project:

* Docker Engine (running)
* Docker Compose
* Git (optional)

---

## ▶️ How to Run the Project

Clone the repository:

```bash
git clone https://github.com/your-username/javaeats.git
cd javaeats
```

Run with Docker:

```bash
docker compose up -d
```

This will:

1. Start MongoDB
2. Build the application
3. Run all services

---

### 🧪 Running Tests

```bash
docker compose -f docker-compose-mongo.yml up
mvn clean package
```

---

## 🌐 Application Access

* API: http://localhost:8081
* Health Check: http://localhost:8081/actuator/health
* Swagger UI: http://localhost:8081/swagger-ui/index.html

---

## 📚 API Documentation

Swagger UI allows exploring and testing all endpoints directly in the browser.

---

## 📺 Presentation

You can see the project running here:
https://www.youtube.com/watch?v=2OjX8FvAW1U

---

## 📝 Notes

This project was developed for academic purposes, applying:

* Clean Architecture
* REST standards
* Design patterns
* Scalable backend practices

---