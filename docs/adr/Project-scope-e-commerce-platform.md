# E-commerce Platform — Project Scope

## Overview

A distributed e-commerce platform built with Java, Spring Boot, Kafka, and Angular.
Each customer company runs its own installation and administers that store; v1 is not a shared multi-tenant service.

## Functional Scope

**Authentication**
- User registration and login.
- JWT-based authentication and refresh tokens.
- Role-based authorization.

**Catalog**
- Products and categories configured by the store administrator.
- Reusable, typed product specifications with normalized units.
- Product images with ordering and a primary image.
- EUR as the store's single base currency in v1.
- Unit sales and estimated-weight pricing: customers order whole pieces, the server estimates weight from average weight per piece, and fixes the amount when the order is created.
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
- Shared multi-tenancy, multiple currencies, promotions/coupons, branch-level inventory, actual-weight repricing after preparation, shipping, and logistics.
- Additional features or infrastructure without a clear engineering benefit.

## Engineering Principles

- **Quality over scope:** Reduce features before compromising testing, clarity, or maintainability.
- **Justified complexity:** Introduce patterns and infrastructure only when they address a concrete problem.
- **Explicit trade-offs:** Document significant architectural decisions, including their rationale, alternatives, and consequences.
- **Failure-aware design:** Account for partial failures, duplicate events, and eventual consistency.
- **Incremental delivery:** Each development phase should leave the system in a demonstrable state.
