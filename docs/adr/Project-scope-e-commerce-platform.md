# E-commerce Platform — Project Scope

## Overview

A distributed e-commerce platform built with Java, Spring Boot, Kafka, and Angular.

## Functional Scope

**Authentication**
- User registration and login.
- JWT-based authentication and refresh tokens.
- Role-based authorization.

**Catalog**
- Products and categories.
- Search, pagination, and filtering.
- MongoDB.

**Inventory**
- Stock management and reservations with expiry.
- PostgreSQL.

**Orders**
- Order creation, state transitions, and purchase lifecycle management.
- PostgreSQL.

**Payments**
- Stripe PaymentIntents in test mode.
- Signed webhook verification.

**Inter-service Communication**
- Asynchronous messaging through Kafka.
- Transactional outbox.
- Idempotent consumers.
- Dead-letter topics.
- Avro event schemas managed through Schema Registry.

**Frontend**
- Angular 22.
- Stripe Elements for checkout.

## Out of Scope

The following are excluded from the initial scope:

- Production payment processing and multiple payment providers.
- Kubernetes; Docker Compose is sufficient for local development.
- Multi-tenancy, internationalization, coupons, shipping, and logistics.
- Additional features or infrastructure without a clear engineering benefit.

## Engineering Principles

- **Quality over scope:** Reduce features before compromising testing, clarity, or maintainability.
- **Justified complexity:** Introduce patterns and infrastructure only when they address a concrete problem.
- **Explicit trade-offs:** Document significant architectural decisions, including their rationale, alternatives, and consequences.
- **Failure-aware design:** Account for partial failures, duplicate events, and eventual consistency.
- **Incremental delivery:** Each development phase should leave the system in a demonstrable state.