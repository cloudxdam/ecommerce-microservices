# ADR-007: Transactional outbox instead of Kafka transactions

**Status:** Accepted

## Context

Publishing an event and changing the database state are two operations in two systems.
A dual write creates consistency risks because the database transaction and Kafka publication 
can succeed or fail independently:

1. DB commits, publish fails → event lost.
2. Publish succeeds, DB rolls back → phantom event.
3. Process dies between DB commit and publish → event lost.
4. Client retries after timeout → duplicate event.

## Decision

The event is written to an `outbox_event` table **within the same transaction** as the business change. 
A `@Scheduled` poller publishes pending rows using `FOR UPDATE SKIP LOCKED`.

## Alternatives

| Alternative                                            | Why not                                                                                                                                          |
|--------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| `kafkaTemplate.send()` inside `@Transactional`         | Dual write. The DB commit and the Kafka commit are not atomic.                                                                                   |
| Spring `KafkaTransactionManager` synchronized with JPA | Works and is faster. **But** if the process dies between the DB commit and Kafka, the event is lost, and there is no way to recover or audit it. |
| The `one.tomorrow.transactional-outbox` library        | A good, proven option, but PostgreSQL-only. It is written by hand to understand the pattern and be able to defend it.                            |

## Consequences

- **Gained:** the event survives a process crash. It is auditable in the database. No silent loss.
- **Lost:** up to `poll-interval-ms` (500 ms) of extra latency. One more table to clean up (or a TTL on it).
- **Assumed:** **at-least-once** delivery, not exactly-once. A row can be published twice if the process 
- dies between `send()` and `markPublished`. That is why consumer idempotency is mandatory, not optional.
- **Assumed:** `FOR UPDATE SKIP LOCKED` lets several publisher workers run without doing the same work 
- twice, but two workers can still pick rows of the same order and publish them out of order — consumers 
- must tolerate that (the order state machine rejects invalid transitions). 
- A publish error keeps the row pending for retry; a permanently failing row needs an alert, not an infinite 
- silent loop.
