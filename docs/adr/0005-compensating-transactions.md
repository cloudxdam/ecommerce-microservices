# ADR-005: Compensating transactions instead of distributed transactions

**Status:** Accepted

## Context

An order touches `orders_db`, `inventory_db` and Stripe. No ACID transaction can span all three.

## Decision

A saga with explicit compensations. Every step has a defined, **idempotent** counter-measure.

## Alternatives

| Alternative                      | Why not                                                                                                                                   |
|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| 2PC / XA                         | Two-phase commit is not supported end-to-end across PostgreSQL, Kafka and Stripe, and would introduce substantial operational complexity. |
| Do nothing (best-effort flow)    | Stock is lost and customers are overcharged without detection.                                                                            |
| Reserve stock only after payment | Avoids one compensation, but allows overselling and makes the customer wait for the charge to learn whether stock exists.                 |                                                                                                                                           |

## Consequences

- **Gained:** business consistency through explicit compensations. Intermediate states are visible, 
- stuck steps are detectable, and a failed compensation is retried and escalated instead of silently ignored.
- **Lost:** compensations must be written and maintained. A compensation that runs twice because of 
- the outbox **cannot** be naive. The saga is not finished until every compensation succeeds or a 
- human intervenes.
- **Assumed:** intermediate states (`PENDING_STOCK`, `AWAITING_PAYMENT`) are visible to the user. 
- That is honest, not a bug.
