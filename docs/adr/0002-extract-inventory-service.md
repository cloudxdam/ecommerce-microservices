# ADR-002: Extract inventory as an independent service

**Status:** Accepted

## Context

Stock has a lifecycle of its own: goods are received, reserved, released and expire. 
Its invariants (`available >= 0`, `reserved + available == total_on_hand`) have nothing to do 
with those of a product.

## Decision

An independent service with PostgreSQL. Owner of `stock_level`, `stock_reservation` and `stock_reservation_line`. 
It knows nothing about prices or payments.

## Alternatives

| Alternative                              | Why not                                                                                    |
|------------------------------------------|--------------------------------------------------------------------------------------------|
| Module inside `orders`                   | Stock is used from several places (UI, admin, orders) and changes at its own pace.         |
| Module inside `catalog`                  | See [ADR-001](0001-stock-does-not-belong-to-catalog.md).                                   |
| Service inside `catalog` with its own DB | Valid, but couples the lifecycle of both services and makes `catalog` no longer cacheable. |                                                                                    |

## Consequences

- Reservation is a local ACID transaction, which is exactly what makes it reliable.
- The smallest service in the project (~400 lines), but the one with the most important invariants.
- An internal `POST /availability/batch` endpoint exists because an N+1 over the network is not acceptable.
