# Xalts Bookings — Ticket Bookings Microservices

This repository contains a complete ticket-booking backend implemented as
independent Spring Boot microservices. It's designed as a learning / demo
project that illustrates service discovery, API gateway patterns, auth with
JWT, and inter-service communication.

**Repository layout**
- discovery-service — Eureka service registry
- gateway-service — Spring Cloud Gateway
- auth-service — Authentication & JWT issuance
- user-service — User profiles and management
- event-service — Events, venues and schedules
- booking-service — Booking orchestration and reservations
- payment-service — Payment processing (simulated)
- notification-service — Email/notification worker

**Tech stack**
- Java 17
- Spring Boot
- Spring Cloud (Eureka, Gateway)
- Spring Security + JWT
- MySQL (or compatible RDBMS)
- Gradle

**Quick links**
- Service folders: [discovery-service](discovery-service) | [gateway-service](gateway-service) | [auth-service](auth-service) | [user-service](user-service) | [event-service](event-service) | [booking-service](booking-service) | [payment-service](payment-service) | [notification-service](notification-service)

**Ports (default)**
- discovery-service: 8761
- auth-service: 8080
- gateway-service: 8081
- user-service: 8082
- event-service: 8083
- booking-service: 8084
- payment-service: 8085
- notification-service: 8086

---

**Prerequisites**
- Java 17 JDK installed and JAVA_HOME set.
- Gradle wrapper is included; no global Gradle required.
- MySQL or other RDBMS for services that require persistence (or configure in-memory H2 for quick runs).

**Configuration**
Each service reads configuration from `src/main/resources/application.yml` or environment variables. For local development you can set overrides using environment variables or create a local `application-local.yml` and point Spring profile to `local`.

Common environment variables (examples):
- `SPRING_DATASOURCE_URL` — JDBC URL for the database
- `SPRING_DATASOURCE_USERNAME` — DB user
- `SPRING_DATASOURCE_PASSWORD` — DB password
- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` — Eureka server URL (discovery-service)

**Database setup (MySQL example)**
1. Create databases for services that require persistence (e.g., `users`, `events`, `bookings`).
2. Update each service's `SPRING_DATASOURCE_URL` to point to your DB.
3. Services use Spring JPA; schema auto-generation can be enabled via `spring.jpa.hibernate.ddl-auto` during development.

---

**Run locally (Windows)**
Open a terminal per service and run the Gradle Boot task. From the repository root, for Windows use the included wrapper:

```powershell
cd discovery-service
.\gradlew.bat bootRun

# In other terminals (start in this order):
cd ..\auth-service
.\gradlew.bat bootRun

cd ..\gateway-service
.\gradlew.bat bootRun

# Then start user, event, booking, payment, notification services similarly
```

Tip: start `discovery-service` first so the other services can register.

**Run locally (Unix / macOS)**
```bash
cd discovery-service
./gradlew bootRun
```

**Build all services**
From the repo root you can run each service's build or script a bulk build. Example per-service:

```bash
cd user-service
./gradlew clean build
```

---

**Testing**
- Unit tests are under each service's `src/test` folder and can be run with `./gradlew test`.
- Integration tests (if present) use local profiles and testcontainers where configured.

**API / Usage**
The API Gateway exposes public endpoints. Authentication is handled by the auth-service which issues JWT tokens.

Common flows:
- Sign up / Sign in (auth-service)
- Browse events (event-service)
- Create booking (booking-service) — booking may reserve seats and call payment-service
- Payment simulation (payment-service) — accepts payment and returns simulated success
- Notifications (notification-service) — sends booking confirmation emails

Each service includes sample controllers and DTOs in `src/main/java`. Refer to individual service README or source for concrete endpoint paths and request formats.

**Architecture (high-level)**

discovery-service (Eureka)
  ↑
Other services → register → discovery-service
  ↓
gateway-service → routes requests to downstream services

Auth: auth-service issues JWT tokens used by gateway/service-to-service calls for authorization.

---

**Development notes**
- Use profiles to separate local vs cloud configs (e.g., `local`, `dev`, `prod`).
- To speed development, use H2 for services that only need simple persistence locally.
- Logs are configured per-service; adjust log levels in `application.yml`.

**Contributing**
- Fork the repo and open a pull request with descriptive changes.
- Run `./gradlew build` in the modified service and ensure tests pass.

**Resources & references**
- See the `docs` folder for the original assignment and design notes: [docs](docs)

**License & contact**
- This project is provided as-is for learning/demo purposes. Add a license file if you intend to publish.
- For questions, open an issue in this repository.

---

End of README
