# Xalts Bookings – Microservices Backend

This repository contains a complete **ticket booking system** implemented using
**Spring Boot microservices architecture**.

## Architecture Overview

The system is composed of multiple independent services communicating via REST
and registered using **Eureka Service Discovery**.

### Microservices

| Service | Description | Port |
|------|-----------|------|
| discovery-service | Eureka server | 8761 |
| auth-service | JWT authentication | 8080 |
| gateway-service | API Gateway | 8081 |
| user-service | User management | 8082 |
| event-service | Events & venues | 8083 |
| booking-service | Booking orchestration | 8084 |
| payment-service | Payments | 8085 |
| notification-service | Email notifications | 8086 |

## Tech Stack
- Java 17
- Spring Boot
- Spring Cloud (Eureka, Gateway)
- Spring Security + JWT
- MySQL
- Gradle

## How to Run
1. Start `discovery-service`
2. Start `auth-service`
3. Start `gateway-service`
4. Start remaining services

Each service can be started using:
```bash
./gradlew bootRun
