# ADR-008: JSON for REST, Avro with Schema Registry for events

**Status:** Accepted

## Context

Two different contract problems, two different formats.

- **REST APIs** are request/response with many possible clients. JSON is the ecosystem default: 
- readable, universally supported, no extra infrastructure.
- **Kafka events** are long-lived contracts consumed by services that do not exist yet. 
- Schema evolution must be managed carefully to prevent compatibility issues between producers and consumers. Avro 
and Schema Registry provide explicit contracts and enforce compatibility rules as event schemas evolve.

Doing JSON first and migrating to Avro later would rework the same lesson twice: 
compatibility modes (BACKWARD/FORWARD) already teach schema evolution directly, without a wire-format 
migration in the middle.

## Decision

- REST contracts use **JSON**.
- Kafka event payloads are **Avro**, with schemas registered in Schema Registry and **BACKWARD** compatibility 
enforced.
- Event keys remain plain strings (`aggregateId`); topic names keep the `.v1` suffix as contract identity.

## Alternatives

| Alternative                     | Why not                                                                                                                                   |
|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| JSON for events too             | Workable at this scale, but contracts are enforced only by convention and tests. Skips the standard tooling the project set out to learn. |
| Avro everywhere, including REST | Non-standard for HTTP APIs; client tooling expects JSON. No benefit for request/response.                                                 |
| Migrate from JSON to Avro later | Possible, but costs a reteach of the same concepts and a serialization rework across all services for no new lesson.                      |

## Consequences

- **Gained:** schemas are checked at publish time; evolution follows declared compatibility rules; binary payloads are smaller; the standard Kafka skill set is part of the project.
- **Lost:** Schema Registry becomes critical infrastructure (one more piece that can fail). Events are not human-readable in kafka-ui without registry integration. Slightly higher onboarding cost.
- **Assumed:** the registry runs as a single node in local development (HA out of scope). The outbox pattern is unaffected — how payloads are stored and serialized at publish time is decided in the Kafka phase. REST stays JSON throughout.
