# Project Brain — MAHAMART Stock Indent

Last updated: 2026-10-12

## One-line goal
Replace handwritten store-to-warehouse stock requests with an offline-first Android app that searches an imported SKU master, records requested quantities, and exports a four-column CSV/Excel indent for manual email submission.

## Current truth
- GitHub repo: `rajuuddanti/Mahamart_Indent`
- Branch: `main`
- The repository started empty.
- Only documentation has been committed; there is no Android app implementation yet.
- User has approved the recommended MVP direction.
- Actual SKU master not yet supplied.
- No builds/tests/APK exist yet.

## Priority order
1. Obtain real master file and map its SKU code/name columns.
2. Create a clean Android Studio/Kotlin project in this repo.
3. Implement reliable local import and search.
4. Implement indent entry and local persistence.
5. Implement exact CSV export; add XLSX.
6. Implement email attachment preparation.
7. Test on a real device and document results.

## Hard constraints
- Keep MVP offline-first and backend-free.
- Do not introduce Supabase or a paid service without user approval.
- Store identity is configured by office and locked on a store device.
- Do not invent store codes, SKU master headers, warehouse email, or Android minimum version.
- Never alter the exact output column names/order without asking.
- A store indent is a request for quantities; it is not a stock-on-hand or warehouse inventory calculation.
- Sending email requires the manager's explicit action in the email client.
- Updating the master must not destroy historical indent data.

## Export contract
Header exactly:
`Store,SKU code,Sku name,QTY`

Each line has the configured store name, SKU code as text, SKU name, and positive quantity. SKU code must retain leading zeros. CSV quoting must handle commas, quotes, newlines, and Unicode. Avoid extra columns.

## Architecture direction
Kotlin Android app; Room local database; DataStore or equivalent for configuration; Android Storage Access Framework for import; local CSV parsing/writing; XLSX export library if maintainable; Android Sharesheet/email intent for attachment preparation. This is proposed architecture, not implemented code.

## Quality bar
- Fast product search.
- Large touch targets and minimal typing.
- Duplicate SKU prevention.
- Draft auto-save and recovery.
- Clear import/export errors.
- No false “email sent” status.
- CSV and historical data remain correct after master replacement.
- Verify every feature on a physical Android phone where possible.

## Standing handoff rule
If user says “push .md files” or asks to hand off the project, update all relevant Markdown docs (HANDOFF, PROJECT_BRAIN, decisions/conversation log, architecture, requirements, checklist, changelog, known issues as appropriate) and push them to GitHub. Include the latest decisions, conversation summary, implementation state, file/commit changes, tests, known bugs, and next steps. Report exact commits. Do not interpret this as updating only one README.
