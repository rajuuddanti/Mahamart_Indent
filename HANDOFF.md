# MAHAMART Stock Indent — Complete Handoff

Last updated: 2026-10-12
Repository: https://github.com/rajuuddanti/Mahamart_Indent
Default branch: main
Status: Documentation initialized; Android app implementation has NOT started yet.

## CRITICAL INSTRUCTION FOR EVERY FUTURE CHAT

When the user says “push .md files”, “push handoff files”, or equivalent, interpret it as a request to update and push ALL project continuity documentation, not merely one README. Update the full handoff package with the latest:
- discussions and user requirements
- decisions and rejected alternatives
- project brain / current state
- architecture and data flow
- feature specifications and UX decisions
- file/repository/branch/commit changes
- bugs, test results, unresolved issues, and risks
- next steps and instructions for resuming in a new conversation

Push every relevant Markdown handoff file to this GitHub repository. Preserve previous context; append or update accurately without inventing decisions. Verify commits and report the exact paths and commit hashes. Before ending a substantial project conversation, keep the handoff docs current if asked. Do not claim implementation or testing happened unless it actually did.

## 1. Product vision

Build a simple, reliable Android Stock Indent app for MAHAMART supermarket stores. Today store managers write SKU names and required quantities on paper and send the request to the warehouse. The app replaces paper with a searchable product master, quantity entry, saved drafts/history, export files, and email preparation.

The first version is intentionally offline-first and does not require a backend, Supabase, or paid hosting.

## 2. Confirmed decisions

- Platform: Android app.
- Initial backend: none; local device storage.
- SKU master: import the user's existing CSV/Excel master. Inspect the actual file before finalizing field mapping.
- Product search: SKU code and SKU name. Barcode search may be supported if a barcode column exists in the real master.
- Store identity: office-configured and locked for each store, to prevent selecting the wrong store. The configuration/distribution method still needs implementation and confirmation.
- Indent: create, edit, remove line, save draft, resume draft, history, re-export.
- Duplicate prevention: one row per SKU per indent; if the same SKU is selected again, edit/update its quantity rather than adding a duplicate.
- Operation: work offline for master search and indent entry.
- Export: CSV and Excel/XLSX where practical.
- Exact export columns and order: `Store,SKU code,Sku name,QTY`. No extra columns.
- Email: prefill the configured warehouse email and attach generated file(s) through Android email/share intent. The user reviews and explicitly sends; do not silently send.
- Warehouse workflow: email-based initially. No warehouse dashboard/app in MVP.
- Initial project repository was empty. Only Markdown specification/checklist files have been added so far. No Android source code, APK, build, or tests have been created yet.

## 3. User's intended CSV format

```csv
Store,SKU code,Sku name,QTY
Mallial,100245,Aashirvaad Atta 5kg,12
Mallial,100567,Surf Excel 1kg,24
Mallial,100890,Tata Salt 1kg,30
```

Examples are illustrative only, not actual SKU master data.

Filename example: `Mallial_Stock_Indent_2026-10-12.csv`; corresponding Excel filename should follow the same pattern.

CSV export requirements:
- Exact header/order as above.
- Correctly escape commas, quotes, and line breaks in SKU names.
- Store column populated from locked store configuration.
- One row per SKU in the indent.
- Quantity must be a positive number; reject invalid values and do not export blank/zero quantities.
- Preserve Unicode product names.
- Ensure spreadsheet-friendly UTF-8 output; evaluate BOM only if needed for Excel compatibility.
- Never lose saved indents when updating/re-importing SKU master.

## 4. Proposed user workflow

1. Office imports SKU master and configures store identity and warehouse email.
2. Store manager opens the app and starts a new indent.
3. Manager searches by SKU code or name and enters required quantity.
4. Manager reviews the list, edits quantities, removes items, and saves a draft.
5. Manager taps Send to Warehouse.
6. App creates CSV and Excel file(s), opens email composer/share sheet with recipient and attachments prepared.
7. Manager reviews and sends email manually.
8. Manager can view old indents and re-export them.

## 5. Planned screens

1. Home: New Indent, Drafts, Previous Indents, Import/Update Master (may be restricted to office/admin).
2. Create Indent: locked store name, SKU search, numeric quantity entry, add/update item.
3. Indent Review: item list, editable quantities, remove item, distinct SKU count, validation.
4. Export & Send: CSV export, Excel export, email/share.
5. Settings/Office Setup: store configuration, warehouse email, master import/update.

Keep UI fast, clear, and suitable for ordinary Android phones. Avoid unnecessary complexity.

## 6. Architecture proposal (not yet implemented)

- Kotlin + Android Studio.
- Room database for SKU master, indent headers, and indent lines.
- DataStore or equivalent for device/store configuration.
- Android Storage Access Framework for selecting/importing a master file.
- CSV parser/writer with proper escaping and sensible handling of large files.
- XLSX library if appropriate; keep dependencies maintained and APK size reasonable. CSV is required even if XLSX is deferred.
- Android Sharesheet/email intent for user-confirmed sending.
- No remote backend for MVP.

Potential data model (to be refined):
- SKU: skuCode (stable key), skuName, optional barcode and other imported metadata.
- Indent: local ID, created/updated timestamps, status (draft/export prepared), store code/name snapshot.
- IndentLine: indent ID, SKU code, SKU name snapshot, positive quantity.
- StoreConfig: locked store code/name, warehouse email, configuration source/version.

Store SKU name snapshots in indent lines so historic indents remain readable if the master changes. Treat SKU code as text to preserve leading zeros.

## 7. Import requirements

- First inspect the user's real master file, its headers, format, encoding, size, and duplicate behavior.
- Do not guess column names or assume sample values are real.
- Map SKU code and SKU name explicitly; allow a mapping screen if headers vary.
- Detect missing code/name, duplicate codes, blank rows, and malformed data; show a useful import summary.
- Master replacement/update must not delete or corrupt existing drafts/history.
- Large masters should be parsed efficiently and imported transactionally where possible.
- Do not require daily re-import. Keep the master local until the office imports a newer one.

## 8. Repository history / current files

Initial repository: empty, public GitHub repo, default branch `main`. Connected GitHub integration confirmed admin/maintain/push permissions.

Files committed:
- `README.md` — app specification, decisions, export contract, suggested architecture, build phases.
  - commit: `543fa9a3504b329232d1a28aec5239dcfa94b3b6`
- `docs/IMPLEMENTATION_CHECKLIST.md` — phases and verification checklist.
  - commit: `187c64ba2e87fefa4ef4aa6f48ca688bf926c0c5`
- `HANDOFF.md` — this continuity document.
  - commit to be recorded in this commit's GitHub result / next handoff update.

No Android project files or Gradle setup exist yet as of this handoff.

## 9. Implementation checklist

### Phase 1: Master import
- [ ] Obtain user's actual SKU master.
- [ ] Inspect file columns/encoding/size.
- [ ] Implement file picker and import.
- [ ] Map code/name fields.
- [ ] Validate duplicates and malformed records.
- [ ] Persist local master and show import summary.
- [ ] Test updating master while drafts/history exist.

### Phase 2: Indent entry
- [ ] Create Android project and establish build configuration.
- [ ] Lock store configuration.
- [ ] Implement search by code/name.
- [ ] Implement numeric quantity entry.
- [ ] Prevent duplicate SKU lines.
- [ ] Edit/remove lines.
- [ ] Autosave and explicit draft handling.
- [ ] Add review/validation UI.

### Phase 3: Persistence/history
- [ ] Room schema and migrations.
- [ ] Draft list and reopen.
- [ ] Previous indent history.
- [ ] Snapshot store/SKU data on indent lines.
- [ ] Re-export saved indents.

### Phase 4: Export/share
- [ ] Exact CSV contract.
- [ ] CSV escaping and Unicode tests.
- [ ] Filename sanitization.
- [ ] XLSX export.
- [ ] Email/share attachment intents.
- [ ] Correct recipient configuration.
- [ ] Explain that sending is completed by the user's email client, not silently by app.

### Phase 5: Testing
- [ ] Import realistic master.
- [ ] Search code/name.
- [ ] Duplicate SKU handling.
- [ ] Invalid quantity validation.
- [ ] CSV with commas, quotes, newlines, Unicode, and leading-zero SKU codes.
- [ ] Offline usage.
- [ ] Master update safety.
- [ ] Save/reopen/re-export.
- [ ] Email attachment validation.
- [ ] Build and test on physical Android device.

## 10. Open questions / dependencies

- Actual SKU master file has not been provided yet.
- Exact master column headers and file size are unknown.
- Store code/name list and how office provisions locked configuration are not yet finalized.
- Warehouse email address(es) are not yet provided.
- Minimum Android version/device target is not yet decided.
- XLSX library choice is not yet decided.
- No real app build or tests exist yet.

Do not block all development on every open question; proceed with a basic Android scaffold where safe, but inspect the real SKU master before locking its import mapping.

## 11. New-conversation resume instructions

When a new chat begins, read this file first, then read `README.md` and `docs/IMPLEMENTATION_CHECKLIST.md`. Verify repository state and latest commits instead of assuming this document is fully current. Continue from the actual code/repo state, preserve all confirmed decisions, and ask only for genuinely missing inputs. The most useful next user input is the real SKU master Excel/CSV file.
