# ADR-004: Choreography for the saga

**Status:** Accepted

## Context

The saga has 6 transitions over a single aggregate (`Order`), and each step depends only on the previous one. 
The happy path is linear; there is no branching business logic across aggregates.

## Decision

Choreography: each service reacts to the events it cares about and publishes its own outcomes. 
`orders-service` does not orchestrate anyone: it owns the order aggregate and its state transitions, and 
the `orders` table reflects the progress of the saga — enough to spot a stuck order without a central 
coordinator.

## Alternatives

| Alternative                              | Why not                                                                                                                                                                                                          |
|------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Orchestration with a coordinator service | An orchestrator earns its place when the flow has logic: conditionals, variable parallelism, dynamic sequences. This one is linear on the happy path and single-aggregate. Adding one would be over-engineering. |
| Pure choreography with no central state  | You would have no saga timeline and no way to detect a stuck order.                                                                                                                                              |

## Consequences

- **Gained:** no service knows the others; adding a new event consumer (say `notification-service`) does not 
require changes to existing services.
- **Lost:** the saga flow is spread across the code of several services. Compensated by the 
- `GET /orders/{id}/saga` diagnostics endpoint and the `correlationId`s.
- **Assumed:** debugging is harder than with an orchestrator. It is a conscious cost.
