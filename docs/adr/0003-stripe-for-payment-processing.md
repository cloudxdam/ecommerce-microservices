# ADR-003: Use Stripe PaymentIntents for payment processing

**Status:** Accepted

## Context

Payment is where the system touches a third party you do not control: it decides when to respond, 
delivers messages at-least-once, and can reject at any moment. That is exactly the problem that 
makes a saga interesting.

## Decision

An independent service that uses the Stripe **PaymentIntents** API in test mode, with a webhook 
verified by signature.

## Alternatives

| Alternative                                            | Why not                                                              |
|--------------------------------------------------------|----------------------------------------------------------------------|
| Mock payment service                                   | Cheap, but proves nothing: real idempotency, signatures, third-party |
| retries and duplicate webhooks never appear in a mock. |                                                                      |
| Stripe inside `orders-service`                         | Mixes two reasons to change, and the PCI scope of Stripe             |
| spreads across the whole order service.                |                                                                      |
| Legacy `Charges` API                                   | Obsolete, no integrated 3DS, no retry over the same intent.          |

## Consequences

- **Gained:** the project touches a real production API, with HMAC verification, idempotency keys, 
- duplicate webhooks and 3DS. Things that cannot be faked.
- **Lost:** a Stripe account (free) is required to run it, and it must be explained in interviews.
- **Assumed:** webhook tests compute the real HMAC; the signature is never faked, because a test 
- with a fake signature tests nothing.
