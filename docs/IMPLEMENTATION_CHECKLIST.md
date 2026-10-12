# Implementation checklist

## Phase 1 — SKU master import
- [ ] Inspect real SKU master headers and file size.
- [ ] Support CSV import from Android file picker.
- [ ] Map SKU code and SKU name using explicit header mapping.
- [ ] Validate duplicate SKU codes and report import errors.
- [ ] Store imported master locally for offline search.
- [ ] On replacement import, preserve saved indents and show import summary.

## Phase 2 — Indent entry
- [ ] Lock store identity in office configuration.
- [ ] New indent and draft autosave.
- [ ] Search by SKU code/name (case-insensitive).
- [ ] Quantity input uses numeric keyboard.
- [ ] Enforce one row per SKU per indent.
- [ ] Edit quantity or remove item.
- [ ] Show distinct SKU count and validate all quantities.

## Phase 3 — Export and email
- [ ] Exact CSV header: Store,SKU code,Sku name,QTY.
- [ ] Escape CSV values per CSV rules.
- [ ] Stable, safe filenames with store and date.
- [ ] XLSX export or tested Excel-compatible fallback.
- [ ] Open email/sharesheet with attachment(s).
- [ ] Never show a sent status until the user confirms; email clients generally do not report reliable send completion back to the app.

## Phase 4 — History and verification
- [ ] List saved drafts and previous indents.
- [ ] Reopen and edit drafts.
- [ ] Re-export old indents.
- [ ] Tests for CSV commas, quotes, Unicode, invalid quantities, duplicate items, offline mode, and master replacement.
- [ ] Test on a physical phone.
