# Wealth Management Platform

A multi-module Spring Boot backend for a wealth management platform with separate service boundaries for authentication,
wealth domain logic, and AI advisory capabilities.

## Tech stack

- Java 21
- Maven 3.9.x
- Spring Boot 4.1.2
- PostgreSQL 17
- Redis 7.4
- Kafka 4.x
- Docker Compose for local infrastructure

## Modules

- `auth-service` – authentication, JWT issuance, refresh token flow, user profile APIs
- `wealth-service` – financial goals, portfolios, holdings, and event publishing
- `ai-advisor-service` – AI advisory, Redis-backed context, Kafka consumers

## Local build

```bash
mvn clean verify
```

## Local environment variables

Copy `.env.example` to `.env` before running the services locally.

```bash
cp .env.example .env
```

## Docker infrastructure

From the project root:

```bash
cd infrastructure
docker compose up -d
```

This starts PostgreSQL, Redis, and Kafka for local development.

## Run services

```bash
mvn -pl auth-service spring-boot:run
mvn -pl wealth-service spring-boot:run
mvn -pl ai-advisor-service spring-boot:run
```

Health endpoints:

- Auth: http://localhost:8081/actuator/health
- Wealth: http://localhost:8082/actuator/health
- AI Advisor: http://localhost:8083/actuator/health

## Phase 2 implemented

The auth service now includes:

- user entity and repository
- password hashing with BCrypt
- JWT access token generation and validation
- refresh-token persistence and rotation
- Spring Security configuration
- registration, login, refresh, and user profile endpoints

## Phase 3 started

The wealth service now includes a first domain model for financial planning:

- investment goals with target amount, risk profile, and status tracking
- portfolios with asset allocation fields
- holdings with security-level metadata
- Flyway migration and repository layer
- controller endpoints for goals, portfolios, and holdings

Example endpoints:

- POST /api/v1/goals
- GET /api/v1/goals?userId={uuid}
- POST /api/v1/portfolios
- GET /api/v1/portfolios?userId={uuid}
- POST /api/v1/portfolios/{portfolioId}/holdings
- GET /api/v1/portfolios/{portfolioId}/holdings

## Notes

This repo is set up for local development. Docker is required for the Postgres/Redis/Kafka containers, and the current
CLI environment here does not include Docker support, so those runtime checks must be executed on a machine with Docker
installed.
