# ADR-006: Partition key = `aggregateId` on every event

**Status:** Accepted

## Context

Kafka guarantees order **within a partition**, not across partitions. A saga applies several steps to the 
same order.

## Decision

All events of an order are published with `key = orderId`, so they always land in the same partition and 
are processed in order.

## Alternatives

| Alternative                        | Why not                                                                                             |
|------------------------------------|-----------------------------------------------------------------------------------------------------|
| No key (round-robin)               | `order.cancelled` can be processed before `order.created`, and stock is reserved                    |
| for a cancelled order. Stock leak. |                                                                                                     |
| Key = `productId`                  | Mixes different orders into one partition and breaks the saga order.                                |
| Key = `userId`                     | A user with many orders creates a hotspot and makes parallelism between that user's orders useless. |

## Consequences

- **Gained:** all events of one order land in the same partition, so a consumer sees them in publication order. 
- Logical correctness does not come from the key alone: the order state machine rejects transitions that are 
- invalid for the current state, and reserve/release operations are idempotent.
- **Lost:** an aggregate cannot process its events in parallel. It is the right trade-off: saga steps are 
- coupled by definition.
- **Assumed:** different users use different keys, so their orders spread across partitions and can be 
- processed in parallel (the partitioner maps keys by hash; no exclusive partition per user is guaranteed). 
- One user generating most of the traffic would create a hotspot; the fix would be `userId:orderId` as the key.
