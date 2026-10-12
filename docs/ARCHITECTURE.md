# Proposed Architecture — MAHAMART Stock Indent

Status: Proposed, not implemented. Last updated 2026-10-12.

## Design principles
- Offline-first; core workflow must not depend on internet.
- Minimal moving parts for the MVP.
- Local data is durable and drafts survive app restarts.
- Store identity is locked to avoid cross-store requests.
- Export contract is stable and exact.
- Historical indents remain intact when master data changes.

## Components

### 1. SKU master import
Use Android Storage Access Framework to select a CSV or supported spreadsheet export. Parse into a staging collection, validate records, then replace/update the local master transactionally only after validation. The real user master must be inspected before choosing exact header mapping and parsing strategy.

### 2. Local persistence
Room database proposed tables:
- `SkuEntity`: SKU code stored as text, name, optional barcode and imported fields.
- `IndentEntity`: ID, created/updated timestamps, state, store code/name snapshot, optional export metadata.
- `IndentLineEntity`: indent ID, SKU code, SKU name snapshot, quantity.
- Configuration may live in DataStore or a dedicated Room table. Avoid storing secrets; no backend credentials are needed for the MVP.

### 3. Search and indent editing
Search local SKU code and name. Use indexed/normalized search if master size requires it. An indent has at most one line per SKU code. Selecting an already-added SKU should open/edit its quantity rather than duplicate it. Quantity must be a positive numeric value.

### 4. Export
CSV is required. Header must be exactly `Store,SKU code,Sku name,QTY`. Escape all text fields using CSV rules; preserve Unicode and leading zeros in SKU codes. Sanitize filenames. XLSX is desired if a stable library is practical; if deferred, CSV remains the guaranteed output.

### 5. Email/share
Create files in app-accessible storage and share through Android Sharesheet/email intent with URI permissions. Prefill warehouse recipient when supported by the selected client. The user must confirm and send. Do not claim delivery or successful send merely because the composer opened.

## Error handling
- Failed import must not partially wipe a working master.
- Invalid/missing SKU codes and names must be reported.
- Duplicate SKU codes in master must be resolved or explicitly reported.
- Invalid/zero/blank quantities block export or are excluded with a clear explanation.
- If email app is unavailable, leave exported files available to save/share another way.
- Never delete old indents when updating master.

## Testing strategy
Unit tests: CSV parsing/escaping, quantity validation, duplicate prevention, filename sanitation, master validation. Instrumentation/manual tests: offline import/search, process restart, draft persistence, master replacement, exports, and email attachment access on a physical device.

## Not decided yet
- Minimum Android SDK.
- Exact master columns and format.
- Store provisioning mechanism.
- Warehouse email address(es).
- XLSX library.
