# ADR-0009: Configurable catalog specifications and estimated-weight pricing

**Status:** Accepted

## Context

Each company runs its own installation and administers its store. The catalog must support different kinds of goods, including products sold by piece whose price is calculated from an estimated weight. Free-form attributes and units make validation and filtering unreliable; allowing price currency or measurement meaning to vary per product also creates ambiguity.

## Decision

- The store administrator manages categories and reusable specification definitions. V1 supports `TEXT`, `NUMBER`, `OPTIONS`, and `BOOLEAN`; number definitions configure integer/decimal acceptance and optional minimum/maximum, measurement definitions also have a physical dimension and canonical unit, while option definitions have a controlled option list. Categories reference definitions; products may provide any subset, but a value is required whenever a specification is included.
- Once a specification is used, its type and meaning are immutable. Input or display units may change only through conversion to the same canonical unit. Incompatible changes require a new definition and product updates.
- A product has a stable UUID, unique SKU, descriptive fields, status (`DRAFT`, `ACTIVE`, `INACTIVE`), ordered image references with one primary image, timestamps, pricing terms, and specification values. SKU may be corrected while the product is a draft and becomes immutable on first publication. Inactive products remain stored and are hidden from public catalog queries.
- `catalog-service` owns its product images. Store administrators upload them through the panel when creating or updating a product. Catalog stores image metadata/references in MongoDB and image bytes in a persistent Docker volume mounted into `catalog-service`; no external object-storage account is required in v1.
- Public product queries return only `ACTIVE` products. A separate paginated admin query exposes products in all three statuses so store administrators can manage drafts and inactive items; route separation clarifies authorization and filters, while indexes and query criteria determine performance.
- The store has one base currency, EUR in v1. A product price has an amount and pricing unit, not an independently selected currency. Orders snapshot the applicable amount, currency, quantity, and estimated weight.
- Price-range filters and price sorting require an explicit `pricingUnit`; products priced per kg are never compared directly with products priced per piece.
- V1 supports estimated-weight pricing: the customer orders whole pieces from a minimum quantity, then may increase quantity one piece at a time; the server estimates total weight from quantity and average weight per piece, calculates the price per kilogram, rounds the order amount to cents, and fixes that amount when the order is created. Inventory continues to reserve whole pieces. The charge is not adjusted to actual prepared weight.
- A category with associated products cannot be deactivated or deleted. The API returns an actionable conflict asking the administrator to reassign its products first. An empty category may be deactivated or deleted.
- Category names may change without changing the UUID or the slug generated at creation; internal references use the UUID, and the stable slug preserves existing URLs.
- The normalized category slug is unique per store. A duplicate returns a conflict; detecting semantic synonyms such as “Computers” and “PCs” remains the store administrator's responsibility.

## Alternatives

| Alternative | Why not |
|---|---|
| Arbitrary untyped attributes and free-text units | They cannot be reliably validated, compared, or filtered. |
| Let each product choose arbitrary currencies and measurement units | Products in the same store could carry inconsistent price and filter semantics. |
| Charge by actual prepared weight | Requires post-order price reconciliation and payment adjustment, which is outside v1. |
| Delete a category with cascading product changes | Risks silent data loss and changes product classification without an explicit admin action. |
| Hosted object storage or externally supplied image URLs | Hosted usage can create variable charges; external URLs make the store depend on third-party hosting. Uploads are kept local to the installation in v1. |

## Consequences

- **Gained:** store owners can model varied product types without a code deployment, and numeric filters compare normalized values. Estimated-weight goods can be sold while inventory remains an integer piece count.
- **Lost:** the catalog and order contracts need explicit pricing terms and snapshots; custom specification definitions need validation and unit conversion rules. The local image volume must be backed up and is not shared between multiple `catalog-service` replicas.
- **Assumed:** estimated weight is a pricing estimate, not a promise of exact physical weight. Promotions, multiple currencies, branch inventory, actual-weight repricing, shipping, and logistics remain outside v1.
