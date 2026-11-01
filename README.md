# E-commerce Microservices

> Work in progress.

This project is under active development.

A microservices-based e-commerce application built with Java 21 and Spring Boot.
It includes a product catalog, inventory management, order processing and payment integration.

The project uses asynchronous communication between services to handle stock reservations,
payment processing and order status updates. It also explores patterns for reliable event
processing, including the transactional outbox and idempotent consumers.

## Technology Stack

- **Backend:** Java 21, Spring Boot 4.1, Spring Cloud 2025.1
- **Messaging:** Apache Kafka 4 (KRaft), Avro, Schema Registry
- **Databases:** PostgreSQL 17, MongoDB 8
- **Payments:** Stripe (test mode)
- **Frontend:** Angular 22
- **Infrastructure:** Docker Compose

## Planned Services

- Service discovery and configuration
- API Gateway
- Authentication
- Product catalog
- Inventory
- Orders
- Payments
- Notifications

## Architecture Documentation

Architectural decisions are documented in [`docs/adr/`](docs/adr/).

## License

[MIT](LICENSE)