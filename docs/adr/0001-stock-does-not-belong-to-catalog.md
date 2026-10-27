# ADR-001: Stock does not belong to the catalog

**Status:** Accepted

## Context

The initial design had `stock` as one more field on `Product` inside `catalog-service` (MongoDB). 
Placing an order called `POST /products/{id}/decrease-stock` from `orders-service`.

That is a **dual write**: two writes to two databases with no transaction spanning both. 
If stock goes down and the order is not saved, the units are lost forever. 
If the client retries after an ambiguous timeout, the stock reduction can be applied twice. 
Charging the customer twice is a different problem, owned by the payment context (see [ADR-003](0003-stripe-for-payment-processing.md)).

## Decision

Extract `inventory-service` as its own bounded context, owner of stock and reservations. 
A new order **does not reserve stock**: it is created in `PENDING_STOCK` and publishes `order.created.v1`. 
Inventory consumes it and reserves in **a single transaction against its own database**.

## Alternatives

| Alternative                                            | Why not                                                       |
|--------------------------------------------------------|---------------------------------------------------------------|
| 2PC / XA between orders and catalog                    | Kafka and Mongo do not support it. Extremely high complexity. |
| Keep stock in catalog + critical section               | A distributed lock across services is not a database,         |
| it is a home-grown transactional layer with more bugs. |                                                               |
| Synchronous order against a new inventory service      | Still a dual write between `orders` and `inventory`.          |

## Consequences

- **Gained:** inventory is internally consistent, the catalog becomes cacheable (its content no longer 
- changes on purchases), and a real saga appears instead of a chain of calls.
- **Lost:** one more service to deploy and monitor. Stock no longer travels with the product: 
- the listing needs an extra call (or a denormalized `stockAvailable`, which is a documented weak reference).
- **Assumed:** eventual consistency between the order and stock. It is a real cost, managed by the state 
- machine and the reconciler.

> **This decision is fundamental to the project's approach to data ownership and consistency.**
> Everything else follows from it.
