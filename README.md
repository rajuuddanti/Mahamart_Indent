# MAHAMART Stock Indent

An offline-first Android app for store managers to prepare warehouse stock requisitions from the existing SKU master and export them for email submission.

## MVP decisions

- Android app, offline-first, with no backend required.
- Office-configured store identity, locked on each store device.
- Import the existing SKU master from CSV or Excel-compatible CSV data.
- Search the local SKU master by SKU code or SKU name.
- Add one line per SKU per indent; editing an existing SKU updates its quantity rather than creating duplicates.
- Save drafts locally and allow editing, resuming, viewing history, and re-exporting.
- Export both CSV and Excel-compatible workbook format when practical.
- Required output columns, in this exact order: `Store,SKU code,Sku name,QTY`.
- Prepare an email to the configured warehouse address with the exported file(s) attached using Android's share/email intent. The manager explicitly reviews and sends; the app does not silently send email.
- Keep previous indents safe when updating the SKU master.

## Initial workflow

1. Office imports the SKU master and configures store name/code and warehouse email.
2. Manager starts a new indent.
3. Manager searches by code/name and enters requested quantities.
4. Manager reviews, edits, removes, or saves as draft.
5. Manager exports CSV and Excel and opens the email composer with attachments.
6. Manager sends the email and can later view/re-export the saved indent.

## Data/export contract

CSV header must be exactly:

```csv
Store,SKU code,Sku name,QTY
```

One row per requested SKU. Store is the configured store for that device. Quantity must be a positive number. Skip blank/zero quantities; reject invalid quantities before export. CSV values must be escaped correctly (commas, quotes, and line breaks in product names).

Suggested filename: `<Store>_Stock_Indent_YYYY-MM-DD.csv` and corresponding Excel file.

## Suggested implementation

- Kotlin + Android Studio.
- Room for local SKU master, indent headers, and indent lines; DataStore or equivalent for store configuration.
- CSV import/export with streaming/parsing safeguards for large masters.
- Android Storage Access Framework for selecting the master file.
- Android Sharesheet/email intent for user-confirmed sending.
- XLSX generation library only if needed; if dependency size/maintenance is an issue, ensure CSV is fully supported and offer an Excel-compatible CSV as fallback.

## Build phases

1. Inspect the supplied real SKU master and map its columns.
2. Build master import, search, and quantity-entry flow.
3. Add Room persistence, draft/history, and duplicate prevention.
4. Implement exact CSV contract and tests for escaping/quantity validation.
5. Add XLSX and email/share attachment support.
6. Test offline behavior, master replacement, and export correctness on a physical Android device.

## Important limitations

The repository was initialized with this specification before the real SKU master or Android starter project was provided. Do not assume column names in the user's actual master until it is inspected. No warehouse backend is part of the first version.
