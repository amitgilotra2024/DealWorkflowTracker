Here is the complete, updated `README.md` with the system architecture diagram fully synchronized to include the **`DealWorkflowController`**, **`DealWorkflowService`**, **`DealStateTransitionEngine`**, and **`AuditLogRepository`** along with the complete DB schema mappings (`deal_cards`, `workflows`, `audit_logs`).

---

# Deal Workflow Tracker

A high-performance, fault-tolerant Spring Boot 3 backend application designed to track and process complex deal workflows with state-machine governance and automated audit trails. Built with Spring Security (JWT), PostgreSQL, Spring Data JPA, and Apache Kafka, featuring robust fault tolerance using Resilience4j (Circuit Breakers, Retries, Exponential Backoff, and Jitter).

---

## Key Features

* **Sealed State Workflow Engine:** Type-safe, sealed-class domain model (`DealState` and `DealEvent`) enforcing strict state transitions (`DRAFT` → `UNDERWRITING` → `COMPLIANCE_CHECK` → `APPROVED` / `REJECTED`).
* **Automated Audit Logging:** Captures all workflow transitions, user identity, timestamps, IP addresses, and User-Agent headers directly to PostgreSQL via `AuditLogRepository`.
* **Authentication & Authorization:** Secure JWT-based stateless authentication integrated with Spring Security and role-based access control (`ADMIN`, `ANALYST`, `VIEWER`).
* **Database & Persistence:** Relational data management using PostgreSQL and Spring Data JPA / Hibernate with bi-directional domain-to-entity mappings.
* **Event-Driven Architecture:** Asynchronous event publishing and consumption via Apache Kafka.
* **Resilience & Fault Tolerance:** Integrated **Resilience4j** to handle broker failures gracefully:
* **Circuit Breaker:** Prevents cascading network failures when Kafka is unreachable.
* **Retry with Exponential Backoff & Jitter:** Automatically retries failed event publishing with randomized wait delays to prevent thundering herd problems.
* **Fallback Handling:** Gracefully captures failures without throwing unhandled exceptions or disrupting user operations.


* **Containerized Infrastructure:** Seamless local container management for Kafka via Docker.

---

## Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.x
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
git clone https://github.com/amitgilotra2024/DealWorkflowTracker.git
cd DealWorkflowTracker/deal-workflow-tracker-backend

```


2. **Start Kafka in Docker**
Run the official Apache Kafka container on port `9092`:
```bash
docker run -d --name kafka -p 9092:9092 apache/kafka:latest

```


3. **Configure Database Settings**
Ensure PostgreSQL is running locally and verify `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/springdb
    username: springuser
    password: springuser

```


4. **Build and Run the Application**
```bash
./gradlew bootRun

```



The application will launch on `http://localhost:8080`.

---

## State Transition Engine & Audit Logging

The core domain model governs state changes via `DealStateTransitionEngine`. It maps state transitions to discrete `DealEvent` actions, appending standard workflow tracking entries and an immutable `AuditLog` entry.

### State Lifecycle

```
[ DRAFT ] ──(SubmitForUnderwriting)──> [ UNDERWRITING ] ──(PassUnderwriting)──> [ COMPLIANCE_CHECK ] ──(Approve)──> [ APPROVED ]
    │                                          │                                       │
    └──(Reject)────────────────────────────────┴──(Reject)─────────────────────────────┴──(Reject)────────────────> [ REJECTED ]

```

* **Terminal States:** `APPROVED` and `REJECTED` are immutable and accept no further transitions.
* **Audit Metadata:** Every transition logs `changedBy`, `oldValue`, `newValue`, `ipAddress`, and `userAgent`.

---

## API Endpoints

### Authentication (`/api/auth`)

| Method | Endpoint | Access Level | Description |
| --- | --- | --- | --- |
| **POST** | `/api/auth/register` | Public | Register a new user account. |
| **POST** | `/api/auth/login` | Public | Authenticate credentials and receive a JWT Bearer token. |

---

### User Management (`/api/users`)

| Method | Endpoint | Access Level | Description |
| --- | --- | --- | --- |
| **GET** | `/api/users/getAllUsers` | Authenticated | Fetch a complete list of registered users. |
| **POST** | `/api/users/createUser` | Authenticated | Direct user entity creation endpoint. |

---

### Deal Cards (`/api/deal-cards`)

| Method | Endpoint | Allowed Roles | Description |
| --- | --- | --- | --- |
| **GET** | `/api/deal-cards/getAll` | `VIEWER`, `ANALYST`, `ADMIN` | Retrieve all deal cards. |
| **POST** | `/api/deal-cards/createDealCard` | `ANALYST`, `ADMIN` | Create a deal card and trigger an async Kafka event. |
| **DELETE** | `/api/deal-cards/{id}` | `ADMIN` | Delete a deal card by its ID. |

---

### Deal Workflows (`/api/deal-workflows`)

| Method | Endpoint | Allowed Roles | State Transition Target | Description |
| --- | --- | --- | --- | --- |
| **POST** | `/api/deal-workflows/{id}/submit` | `ANALYST`, `ADMIN` | `UNDERWRITING` | Transitions deal from `DRAFT` to `UNDERWRITING`. |
| **POST** | `/api/deal-workflows/{id}/pass-underwriting` | `ANALYST`, `ADMIN` | `COMPLIANCE_CHECK` | Transitions deal from `UNDERWRITING` to `COMPLIANCE_CHECK`. |
| **POST** | `/api/deal-workflows/{id}/approve` | `ADMIN` | `APPROVED` | Final approval transition from `COMPLIANCE_CHECK`. |
| **POST** | `/api/deal-workflows/{id}/reject` | `ANALYST`, `ADMIN` | `REJECTED` | Rejects the deal card from any active state. |

---

## System Architecture Design

```
                              [ Client / Frontend ]
                                        │
                                        │ (HTTP REST / Bearer Token)
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           SPRING BOOT 3 BACKEND SYSTEM                          │
│                                                                                 │
│   ┌─────────────────────────────────────────────────────────────────────────┐   │
│   │                         Presentation Layer                              │   │
│   │  - AuthController (/api/auth)                                           │   │
│   │  - UserController (/api/users)                                          │   │
│   │  - DealCardController (/api/deal-cards)                                 │   │
│   │  - DealWorkflowController (/api/deal-workflows)                         │   │
│   └────────────────────────────────────┬────────────────────────────────────┘   │
│                                        │                                        │
│                                        ▼                                        │
│   ┌─────────────────────────────────────────────────────────────────────────┐   │
│   │                          Security Layer                                 │   │
│   │  - JwtAuthenticationFilter (Extracts Bearer token, sets SecurityContext)│   │
│   │  - SecurityConfig (@PreAuthorize Role Guards: ADMIN, ANALYST, VIEWER)    │   │
│   └────────────────────────────────────┬────────────────────────────────────┘   │
│                                        │                                        │
│                                        ▼                                        │
│   ┌─────────────────────────────────────────────────────────────────────────┐   │
│   │                         Business Service Layer                          │   │
│   │  - DealCardService / UserService / DealWorkflowService                  │   │
│   │  - DealStateTransitionEngine (Domain Driven Rules)                      │   │
│   └───────────────────┬─────────────────────────────────┬───────────────────┘   │
│                       │                                 │                       │
│                       ▼                                 ▼                       │
│   ┌───────────────────────────────┐   ┌─────────────────────────────────────┐   │
│   │       Persistence Layer       │   │        Event Pipeline Layer         │   │
│   │  - UserRepository             │   │  - DealEventProducer                │   │
│   │  - DealCardRepository         │   │    [@CircuitBreaker]                │   │
│   │  - AuditLogRepository         │   │    [@Retry + Backoff + Jitter]      │   │
│   │  - PostgreSQL Driver          │   │                                     │   │
│   └───────────────┬───────────────┘   └──────────────────┬──────────────────┘   │
└───────────────────┼──────────────────────────────────────┼──────────────────────┘
                    │                                      │
                    ▼                                      ▼
           ┌─────────────────┐                    ┌─────────────────┐
           │ PostgreSQL DB   │                    │ Apache Kafka    │
           │ (port 5432)     │                    │ Broker Container│
           │ - deal_cards    │                    │ (port 9092)     │
           │ - workflows     │                    └────────┬────────┘
           │ - audit_logs    │                             │
           └─────────────────┘                             ▼
                                                  ┌─────────────────┐
                                                  │ DealEventConsumer│
                                                  │ (DLQ Recoverer) │
                                                  └─────────────────┘

```

---

## Resilience & Fault Tolerance Strategy

The project implements a layered resilience pipeline inside `DealEventProducer`:

1. **Synchronous Transport Verification:** Executes `.get()` on Kafka's `CompletableFuture` to guarantee broker exceptions are caught synchronously within the request thread.
2. **Exponential Backoff with Jitter:** Configured via `application.yml` to retry failed operations up to 3 times, doubling wait intervals (`1s -> 2s -> 4s`) with added randomized variance.
3. **Circuit Breaker State Machine:** Trips to an `OPEN` state if 50% of the last 10 calls fail, short-circuiting network calls for 10 seconds before transitioning to `HALF_OPEN`.
4. **Fallback Handler:** Invokes `publishDealEventFallback(...)` upon failure or when the circuit is open to ensure high application availability.

---

## Data & Execution Flow Sequence

1. **Client Request:** Client issues a transition request (e.g., `POST /api/deal-workflows/5/submit`) with a Bearer JWT.
2. **Authentication & Authorization:** `JwtAuthenticationFilter` validates token claims and checks `@PreAuthorize` permissions.
3. **Engine Evaluation:** `DealWorkflowServiceImpl` retrieves the deal entity, converts it to its `DealState` domain representation, and passes it to `DealStateTransitionEngine`.
4. **State Transition & Persistence:** The new state is applied back to the entity alongside a new `Workflow` history record. Changes are persisted to PostgreSQL within a single `@Transactional` boundary.
5. **Audit Logging:** An `AuditLog` entry is generated and stored directly into the `audit_logs` table containing user identity, state delta, and client network details.
6. **Async Event Publishing:** `DealEventProducer` streams downstream notification events to Kafka through Resilience4j circuit breakers and retries.
