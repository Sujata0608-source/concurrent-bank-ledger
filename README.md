# Concurrent Bank Ledger System

A backend banking ledger application built with Java and Spring Boot to explore **transaction integrity, concurrent fund transfers, database locking, and reliable financial operations**.

The project models transfers between bank accounts while addressing a fundamental backend engineering challenge: ensuring that concurrent transactions do not corrupt account balances or result in inconsistent financial state.

## Key Features

- **Transactional Fund Transfers:** Transfer funds between accounts while maintaining consistent debit and credit operations.
- **Concurrency Control:** Use database-level pessimistic locking to protect account balances during simultaneous transfers.
- **Deadlock Prevention:** Apply deterministic account-lock ordering when acquiring multiple account locks.
- **Relational Persistence:** Store account data in PostgreSQL using Spring Data JPA and Hibernate.
- **RESTful API:** Expose account management and fund transfer operations through HTTP endpoints.
- **Containerized Database:** Run PostgreSQL in Docker using Docker Compose, with a persistent volume for database storage.
- **Concurrent Testing:** Simulate simultaneous transactions using Java's `ExecutorService` and `CountDownLatch` to validate correctness under contention.

*Implementation status: Features marked above should be retained as completed only after they have been implemented and verified. The REST API and concurrent testing are part of the planned implementation until completed.*

## Tech Stack

| Category | Technologies |
|---|---|
| Language | Java 17 |
| Backend Framework | Spring Boot |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL |
| API | REST, HTTP, JSON |
| Concurrency | ExecutorService, CountDownLatch |
| Database Transactions | ACID properties, pessimistic locking |
| Containerization | Docker, Docker Compose |
| Build Tool | Maven |
| Version Control | Git, GitHub |

## Architecture

The application follows a layered architecture that separates HTTP handling, business logic, data access, and persistence.

```text
                  Client
              Postman / HTTP
                     |
                     v
            REST Controller
                     |
                     v
               LedgerService
          (Business Logic & Transactions)
                     |
                     v
            AccountRepository
              (Spring Data JPA)
                     |
                     v
              Hibernate ORM
                     |
                     v
                PostgreSQL
              Docker Container
```

### Layer Responsibilities

- **Controller:** Accepts HTTP requests, validates request data, and returns appropriate HTTP responses.
- **Service:** Implements banking operations and defines transaction boundaries.
- **Repository:** Provides database access through Spring Data JPA.
- **Hibernate:** Maps Java entities to relational tables and generates SQL.
- **PostgreSQL:** Persistently stores account information and enforces database-level consistency.

## Engineering Challenges

### 1. Concurrent Fund Transfers

When multiple requests attempt to modify the same account simultaneously, race conditions can cause incorrect balances.

For example, two transactions might read the same initial balance before either transaction updates it.

The project uses database-level pessimistic locking to coordinate access to account records and prevent conflicting updates.

### 2. Deadlock Prevention

A transfer can involve locking two accounts. If concurrent transfers acquire those locks in opposite orders, they can create a deadlock.

The design uses a deterministic account-lock ordering strategy to reduce this risk.

### 3. Transaction Integrity

A transfer requires both a debit and a credit. These changes must be treated as one logical operation.

Database transactions ensure that a transfer either completes successfully or rolls back when an error occurs, preventing a partially applied transfer.

### 4. Persistent Database Infrastructure

PostgreSQL runs in a Docker container managed through Docker Compose.

A named Docker volume preserves database files across container restarts and recreation, avoiding dependence on application memory for persistent storage.

## Getting Started

### Prerequisites

Install the following:

- Java 17
- Docker Desktop with Docker Compose
- Git
- IntelliJ IDEA or another Java IDE

### 1. Clone the Repository

```bash
git clone https://github.com/Sujata0608-source/concurrent-bank-ledger.git
cd concurrent-bank-ledger
```

### 2. Start PostgreSQL

From the directory containing `docker-compose.yml`, run:

```bash
docker compose up -d
```

Verify that the database container is running:

```bash
docker ps
```

### 3. Configure the Application

Configure the PostgreSQL connection in `src/main/resources/application.properties`.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ledgerdb
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

Set the `DB_PASSWORD` environment variable to the password configured for PostgreSQL in your local Docker setup.

Do not commit real passwords or other secrets to GitHub.

### 4. Run the Application

Using the Maven wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, open the project in IntelliJ IDEA and run the Spring Boot application class.

### 5. Stop the Database

When finished, run:

```bash
docker compose down
```

This stops and removes the Compose-managed containers and network. The named database volume remains available unless explicitly removed.

**Note:** API endpoints and example requests will be documented here as the REST API implementation is completed.

## Testing Strategy

The project is designed to validate both ordinary banking operations and concurrent execution.

Planned validation includes:

- Verifying account creation and retrieval.
- Testing successful fund transfers.
- Confirming that invalid transfers do not modify balances.
- Verifying that failed transactions roll back appropriately.
- Simulating concurrent transfers against shared accounts.
- Checking that final balances match the expected results.
- Verifying that total balances remain consistent across successful transfers.

Performance and concurrency results will be documented after executing the corresponding tests.

## Future Improvements

- Idempotent transfer requests to prevent duplicate processing during retries.
- Correlation IDs and structured request logging for improved traceability.
- Integration tests for transaction rollback and concurrent execution.
- API validation and centralized exception handling.
- Automated CI checks for builds and tests.

## What This Project Demonstrates

This project explores practical backend engineering concepts, including:

- Java application development and object-oriented design.
- Spring Boot application architecture.
- REST API design and HTTP communication.
- ORM-based persistence and relational database design.
- Transaction management and ACID guarantees.
- Database locking and concurrent transaction processing.
- Docker-based development environments.
- Testing correctness under concurrent workloads.

---

**Author:** Sujata Rajak

**GitHub:** [Sujata0608-source](https://github.com/Sujata0608-source)
