# Deal Workflow Tracker

A high-performance, fault-tolerant Spring Boot 3 backend application designed to track and process deal workflows asynchronously. Built with Spring Security (JWT), PostgreSQL, Spring Data JPA, and Apache Kafka, featuring robust fault tolerance using Resilience4j (Circuit Breakers, Retries, Exponential Backoff, and Jitter).

---

## Key Features

* **Authentication & Authorization:** Secure JWT-based stateless authentication using Spring Security.
* **Database & Persistence:** Data persistence using PostgreSQL and Spring Data JPA / Hibernate.
* **Event-Driven Architecture:** Asynchronous event publishing and consumption via Apache Kafka.
* **Resilience & Fault Tolerance:** Integrated **Resilience4j** to handle broker failures gracefully:
  * **Circuit Breaker:** Prevents cascading network failures when Kafka is unreachable.
  * **Retry with Exponential Backoff & Jitter:** Automatically retries failed event publishing with randomized wait delays to prevent system overload.
  * **Fallback Handling:** Gracefully handles unexpected messaging drops without throwing unhandled exceptions.
* **Containerized Infrastructure:** Easily runs local dependencies (Kafka) using Docker.

---

## Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.2.1
* **Build Tool:** Gradle
* **Database:** PostgreSQL
* **Messaging:** Apache Kafka
* **Resilience:** Resilience4j (Circuit Breaker & Retry)
* **Security:** Spring Security & JJWT (JSON Web Token)
* **OR Mapping:** Hibernate ORM / Spring Data JPA
* **Utilities:** Lombok, Spring AOP

---

## Getting Started

### Prerequisites

Ensure you have the following installed locally:
* **Java Development Kit (JDK 21+)**
* **Docker Desktop** (with WSL2 enabled on Windows)
* **Gradle** (or use the included `./gradlew` wrapper)
* **PostgreSQL Database** running on port `5432`

---

### Local Setup & Installation

1. **Clone the Repository**
   ```bash
   git clone [https://github.com/amitgilotra2024/DealWorkflowTracker.git](https://github.com/amitgilotra2024/DealWorkflowTracker.git)
   cd DealWorkflowTracker/deal-workflow-tracker-backend
