# 🍽️ JavaEats
## 📌 Overview

JavaEats is a shared restaurant management system designed to reduce operational costs and standardize processes across multiple establishments. The platform provides a REST API that allows restaurants to manage their data while enabling clients to access information and interact with restaurants.
The system was developed following Clean Architecture principles, ensuring clear separation of concerns, maintainability, and scalability.

## 🏗️ Architecture

The application adopts Clean Architecture, organized into well-defined layers: Core, Domain models, Use cases (business rules), Infrastructure (Infra), REST API controllers MongoDB persistence using Spring Data, Application configuration and observability. This approach ensures that business rules remain independent from frameworks and infrastructure details.

## 🚀 Technology Stack

- Language: Java 21
- Framework: Spring Boot
- Data Persistence: Spring Data
- Database: MongoDB
- Build Tool: Maven (JAR packaging)
- Service Orchestration: Docker Compose
- Observability: Spring Actuator
- API Documentation: Swagger (OpenAPI)

## 📦 Requirements

Before running the project, make sure you have the following installed:

Docker Engine (running)

Docker Compose

Git (optional, to clone the repository)

## ▶️ How to Run the Project

Clone the repository:

```
git clone https://github.com/your-username/javaeats.git
cd javaeats
```

Ensure that the Docker Engine is running.

From the project root directory (where the docker-compose.yml file is located), run:
```
docker compose up -d
```

This command will:

Start the MongoDB container

Build and run the Spring Boot application

Launch all services in detached mode

## 🌐 Application Access

- REST API: http://localhost:8081

- Health Check (Actuator): http://localhost:8081/actuator/health

- Swagger UI (API Documentation):
  http://localhost:8080/swagger-ui/index.html

## 📚 API Documentation

The API is fully documented using Swagger UI, allowing users to explore and test endpoints directly through the browser.

## 📝 Notes

This project was developed for academic purposes, following software architecture best practices, REST standards, and clean code principles.
