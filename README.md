# 🍽️ JavaEats

## 📌 Overview

**JavaEats** is a shared restaurant management platform designed to reduce operational costs and standardize processes across multiple establishments.

The system exposes a REST API that allows:

* Restaurants to manage menus, orders, and operations
* Clients to browse restaurants, place orders, and track deliveries
* Owners and admins to control the full order lifecycle and payments

Key capabilities introduced in **Phase 3**:

* Complete order flow with status transitions (created → awaiting payment → paid → preparing → out for delivery → delivered)
* Payment processing integrated with an external payment simulator
* JWT-based authentication and role-based authorization (CLIENT, OWNER, ADMIN)
* Event-driven messaging via Apache Kafka
* Resilience patterns (circuit breaker, time limiter) with Resilience4j

The application follows **Clean Architecture** principles, ensuring separation of concerns, maintainability, and scalability.

---

## 🏗️ Architecture

The system adopts **Clean Architecture + Ports & Adapters (Hexagonal Architecture)**.

### 📐 Layers

* **Core**

  * Domain (entities, value objects, enums)
  * Use Cases:

    * `in` → input ports (contracts)
    * `impl` → business logic
    * `out` → output ports (gateways)

* **Infrastructure (Infra)**

  * Web (REST Controllers + DTOs)
  * Persistence (MongoDB adapters)
  * Messaging (Kafka publishers + consumers)
  * External integrations (Payment gateway adapter)
  * Security (JWT filter + Spring Security)

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

### ⚡ Event-Driven Architecture (Apache Kafka)

The system uses **Apache Kafka** for asynchronous event-driven communication between bounded contexts.

**Published events:**

| Event | Topic | Trigger |
|---|---|---|
| `OrderCreatedEvent` | `pedido.criado` | After a new order is persisted |
| `PaymentApprovedEvent` | `pagamento.aprovado` | After a payment is confirmed by the external processor |
| `PaymentPendingEvent` | `pagamento.pendente` | When the external processor returns a pending status |

**Consumers:**

* `OrderCreatedConsumer` — reacts to new orders (e.g., for downstream processing)
* `PaymentApprovedConsumer` — updates order status upon payment confirmation
* `PaymentPendingConsumer` — retries the external payment processor with a configurable back-off delay (`payment.reprocessing.fixed-delay-ms`, default 30 s)

Topic names and consumer groups are configurable via `application.properties`.

---

## 🔐 Security

### Authentication

Users authenticate via **`POST /auth/login`** with login + password, receiving a **JWT** (HS256, Base64-encoded secret). The token must be sent in the `Authorization: Bearer <token>` header for all protected endpoints.

### Role-Based Authorization

| Endpoint | Allowed Roles |
|---|---|
| `POST /users` | public |
| `POST /auth/login` | public |
| `GET /restaurants/**`, `GET /restaurants/*/menus` | public |
| `POST /orders` | CLIENT |
| `GET /orders/me` | CLIENT, OWNER |
| `GET /orders` | ADMIN |
| `PATCH /orders/{id}/accept\|preparing\|out-for-delivery\|deliver` | OWNER, ADMIN |
| `PATCH /orders/{id}/reject` | CLIENT, OWNER, ADMIN |
| `PATCH /orders/{id}/payments/{pid}/paid\|failed\|refunded` | OWNER, ADMIN |
| `POST /restaurants` | OWNER, ADMIN |

The `GET /orders/me` endpoint is context-aware: CLIENTs see their own orders, OWNERs see orders placed at their restaurants.

---

## 🔄 Main Flows

### 🛒 Order Lifecycle

1. Authenticated CLIENT sends `POST /orders` (the `clientId` is extracted from the JWT)
2. The use case validates items, menu, and restaurant ownership
3. The order is persisted with status `CREATED`
4. An `OrderCreatedEvent` is published to Kafka
5. Status transitions are performed via dedicated `PATCH` endpoints:

   `CREATED` → `AWAITING_PAYMENT` → `PAID` → `PREPARING` → `OUT_FOR_DELIVERY` → `DELIVERED`

   Rejection (`CANCELED`) is allowed from most states.

---

### 💳 Payment Processing

1. `POST /orders/{orderId}/payments` creates a payment and triggers the external payment processor
2. The external processor (Docker image `erickemprobr/procpag`) returns `APPROVED` or `PENDING`
3. If **APPROVED**: a `PaymentApprovedEvent` is published → order moves to `PAID`
4. If **PENDING**: a `PaymentPendingEvent` is published → the `PaymentPendingConsumer` retries after a configurable delay
5. Manual status overrides are available via `PATCH .../paid`, `.../failed`, `.../refunded`

---

## 🛡️ Resilience

The architecture includes concrete resilience mechanisms:

* **Resilience4j Circuit Breaker** on the external payment processor — opens after 50 % failure rate over a sliding window of 5 calls, auto-transitions to half-open after 10 s
* **Resilience4j Time Limiter** — 3 s timeout for external payment calls
* Asynchronous retry of pending payments via Kafka consumer back-off
* Decoupling through interfaces (Gateways) — infrastructure failures do not leak into the core
* Centralized exception handling (`GlobalExceptionHandler`)
* Stateless architecture (JWT, no server-side sessions)

---

## 🗂️ Project Structure

```
core/
 ├── domain/
 │    ├── enums/
 │    ├── exception/
 │    ├── model/
 │    ├── security/
 │    └── valueobjects/
 ├── usecase/
 │    ├── in/        (input ports)
 │    ├── out/       (output ports / gateways)
 │    └── impl/      (business logic)

infra/
 ├── config/
 ├── messaging/
 │    └── kafka/
 │         ├── config/
 │         ├── consumer/
 │         ├── event/
 │         └── publisher/
 ├── persistence/
 │    ├── adapter/
 │    ├── documents/
 │    ├── mapper/
 │    └── repository/
 ├── security/
 │    └── filter/
 └── web/
      ├── controller/
      ├── dto/
      ├── exception/
      └── mapper/
```

---

## 📊 Architecture Documentation

Detailed architecture documentation (C4 Model + diagrams + flows) is available at:

* `docs/architecture.md`
* `docs/c4/` — Context, Container, Component, and Code diagrams (PlantUML)
* `docs/diagrams/` — Sequence diagrams

---

## 🚀 Technology Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.x |
| Data Persistence | Spring Data MongoDB |
| Database | MongoDB 7 |
| Security | Spring Security + JJWT (JWT HS256) |
| Messaging | Spring Kafka + Apache Kafka 4.0 (KRaft) |
| Resilience | Resilience4j (Circuit Breaker + Time Limiter) |
| External Payment | Simulador FIAP (`erickemprobr/procpag`) |
| Build Tool | Maven |
| Test Coverage | JaCoCo |
| Containerization | Docker / Docker Compose |
| Observability | Spring Actuator |

---

## 📦 Requirements

Before running the project:

* Docker Engine (running)
* Docker Compose
* Git (optional)

No local Java or Maven installation is needed — the application is built inside the Docker image.

---

## ▶️ How to Run the Project

Clone the repository:

```bash
git clone https://github.com/your-username/javaeats.git
cd javaeats
```

Run with Docker Compose:

```bash
docker compose up -d
```

This will start four services:

1. **MongoDB** — database (host port `27018`)
2. **Kafka** (KRaft mode) — message broker (host ports `9092` / `9094`)
3. **Payment Simulator** — external payment processor (host port `8089`)
4. **App** — Spring Boot application (host port `8081`)

### Environment Variables

The `app` container uses the following environment variables (defaults are set in `docker-compose.yml`):

| Variable | Description |
|---|---|
| `SPRING_DATA_MONGODB_URI` | MongoDB connection string |
| `JWT_SECRET_BASE64` | Base64-encoded HS256 secret (min 256 bits) |
| `EXTERNAL_PAYMENT_BASE_URL` | URL of the payment simulator |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka broker address |
| `KAFKA_CONSUMER_GROUP_ID` | Kafka consumer group ID |

---

### 🧪 Running Tests Locally

To run unit tests without the full stack, start only MongoDB:

```bash
docker compose -f docker-compose-mongo.yml up -d
mvn clean package
```

End-to-end testing of payment and messaging flows requires the full Docker Compose stack (Kafka + payment simulator).

---

## 📚 API Documentation

A Postman collection is also available in the repository root:

* **Collection:** `Tech Challenge 3 – G29 - JavaEats V3.6 (docker - 8081).postman_collection.json`
* **Environment:** `Tech Challenge – JavaEats V3.6 (8081).postman_environment.json`

---

## 📺 Presentation

You can see the project running here: https://www.youtube.com/watch?v=Va5EO5NqMoc

---

## 📝 Notes

This project was developed for academic purposes, applying:

* Clean / Hexagonal Architecture
* REST standards
* Event-driven design with Kafka
* JWT authentication and role-based access control
* Resilience patterns (circuit breaker, time limiter)
* Scalable backend practices

---
