# Decisions and Conversation Log

Last updated: 2026-10-12

This file preserves the important discussion and decisions that led to the MAHAMART Stock Indent app. It is a project continuity summary, not a verbatim transcript of every message.

## User's original problem

Store managers currently write down SKU names and required quantities and send the request to the warehouse for stock disbursal. The user wants an app to replace this manual paper process.

Original required output columns:
- Store
- SKU code
- Sku name
- QTY

## Conversation decisions

### 2026-10-12 — Initial product idea
User described an app that:
- Imports their SKU master.
- Lets a store manager enter SKUs and required quantities.
- Exports entered SKUs into Excel.
- Emails the output to the warehouse.
- Outputs the four fields Store | SKU code | Sku name | QTY.

Assistant proposed an offline-first Android stock indent app.

### 2026-10-12 — User approved recommendations
User said to proceed with the recommendations. Decisions proposed and accepted:
- Android app, offline-first.
- Import existing SKU master (CSV/Excel).
- Search by SKU code and SKU name; barcode search only if the master contains a barcode.
- Office-configured store identity locked to each store.
- Avoid duplicate SKU rows within a single indent.
- Draft saving, edit/remove, indent history, re-export.
- Export CSV and Excel.
- Exact four-column export format.
- Email composer opens with warehouse address and attachment(s); user manually confirms sending.
- Email-based warehouse process initially, without a separate warehouse app/dashboard.
- No backend/Supabase/server needed for the MVP.
- Obtain and inspect the real SKU master before deciding exact import mappings.

### 2026-10-12 — GitHub repository
User provided:
https://github.com/rajuuddanti/Mahamart_Indent.git

GitHub connection found repository:
- Name: Mahamart_Indent
- Owner: rajuuddanti
- Default branch: main
- Public repository
- Repository was empty at first connection.
- Push/admin permissions available to the connected GitHub tool.

Initial documentation commits:
- README.md: `543fa9a3504b329232d1a28aec5239dcfa94b3b6`
- docs/IMPLEMENTATION_CHECKLIST.md: `187c64ba2e87fefa4ef4aa6f48ca688bf926c0c5`
- HANDOFF.md: `bad1b9e242c305333bece5fadb11d25b96c16858`
- docs/DECISIONS_AND_CONVERSATION_LOG.md: this conversation-log commit; verify current GitHub history for exact SHA.

### 2026-10-12 — Handoff expectation clarified
User explicitly clarified that whenever they say “push .md files”, they mean push **all handoff/continuity Markdown files**, not merely one .md:
- discussions and conversation summaries
- decisions
- project brain/current state
- architecture
- changes and repo history
- requirements and feature behavior
- bugs/test results/open questions
- next steps and instructions so a new conversation can resume the project from GitHub.

This is a standing project workflow instruction. Every time the user requests handoff/.md pushes, update the entire documentation package, preserve prior decisions, and push the relevant Markdown files. Verify and report actual paths and commit hashes. Never claim code changes or tests that did not happen.

## Product requirements and non-goals

### Required
- Offline SKU lookup and indent entry.
- Locked store identity.
- Local draft persistence and history.
- Exact CSV export columns and order: `Store,SKU code,Sku name,QTY`.
- Excel export, subject to practical implementation.
- Email/share with files attached and recipient prefilled.
- Explicit human action to send.
- Correct CSV escaping and Unicode handling.
- Safe master update without losing indents.

### Not in initial version
- Supabase or any required online backend.
- Warehouse dashboard or warehouse app.
- Automatic/silent email delivery.
- A product inventory balance calculation: this app records requested quantities only unless the user asks for more.

## Resume protocol for future chats

1. Inspect repository's latest state and recent commits.
2. Read HANDOFF.md, README.md, docs/IMPLEMENTATION_CHECKLIST.md, and this log.
3. Compare docs against actual code and update them if needed.
4. Continue from the current implementation; don't recreate or overwrite working code blindly.
5. The first important missing input is the user's real SKU master Excel/CSV.
6. Keep the app simple and do not add a backend without a clear requirement.
