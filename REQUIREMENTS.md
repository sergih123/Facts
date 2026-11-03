# Facts: requirements

A personal archive of charts, graphs, screenshots and links for quickly backing up points while arguing in person. **Speed is the top priority.**

Status: ✅ built in the web version · 🔜 planned for native

## 1. Launch and search
- ✅ The app opens straight into search, with the search box focused.
- ✅ Android: the keyboard opens automatically on launch and when you come back to the app.
- ✅ Results update on every keystroke. The local index loads before any network call.
- ✅ Search covers the title, notes, source/URL, keywords, the description Claude wrote, and the text Claude read from the image.
- ✅ Ranking: field weights are title > keywords/notes > source > description > image text. A match at the start of a word scores higher.
- ✅ Items don't have to match every word. More matched words rank higher, and long queries must match at least a third of their words. Example: "USA elections trump" → "Trump caught lying ahead of midterm elections".
- ✅ Search ignores case and accents and does light stemming (elections→election, lies→ly).
- ✅ Matched words are highlighted in the title and in a snippet from the text.
- ✅ With an empty search, newest items come first.

## 2. Adding items
- ✅ Images: + button, paste, or drag and drop. Several at once are added one by one ("Image 1 of 3, Save & next").
- ✅ Links: 🔗 button, or paste a URL anywhere. Shown as a card with the domain name.
- ✅ **Title is required** for every item.
- ✅ Optional notes (the point it proves, context) and source.
- ✅ Images are compressed on the device: full size max 2200px JPEG, thumbnail 520px.
- ✅ Indexing with Claude (on by default, can be turned off per upload):
  - Images: reads all text in the image, writes a 1–3 sentence description, finds the source, adds up to 18 keywords in English, Spanish and German.
  - Links: description and keywords based on the URL, title and notes (Claude can't open the page).
- 🔜 Native: on-device text reading (ML Kit) so indexing works offline and for free; Claude stays optional for keywords.
- 🔜 Native: Android share target ("Share → Facts" from any app or browser).

## 3. Viewing
- ✅ Full image (double tap to zoom), title, source, notes, description, keywords and image text.
- ✅ Tapping a keyword searches for it.
- ✅ Edit title, notes, source, link and keywords.
- ✅ Copy title, link and notes. Re-run indexing. Delete, with confirmation.
- ✅ Android back button / Escape closes the viewer.

## 4. Compare
- ✅ Opened from any image ("Compare"); pick another saved image (searchable) or one from the phone.
- ✅ A switcher at the top picks one of three modes: **Transparency | Multiply | Split**. Swap works in every mode.
- ✅ **Transparency** and **Multiply** (overlay modes):
  - The top image starts at 50%.
  - Drag to move. Pinch to resize, keeping proportions. Corner handle stretches freely (to align axes). Mouse wheel zooms on desktop.
  - Reset puts the overlay back where it started.
  - Transparency: the slider sets the top image's opacity.
  - Multiply: result = bottom × top, so white backgrounds disappear. The slider crossfades: left = bottom only, middle = both at full strength, right = top only.
- ✅ **Split:** one chart above the other in portrait, side by side in landscape.
- ✅ The last mode used is remembered.
- 🔜 Save a comparison as a new item.

## 5. Storage and sync
- ✅ Local-first: IndexedDB on the device holds metadata and image blobs, so the app opens instantly and works offline.
- ✅ Cloud: each user's private store (`data/users/<id>/<itemId>`) plus uploaded images and thumbnails. Changes and deletions sync between devices.
- ✅ Sync status shown: "synced", "syncing N…" or "saved on this device".
- ✅ Backup: menu (⋮) → Export backup (JSON file with images; Android opens the share sheet, so you can save it to Drive/Files) and Import backup (merges, skips items already there). Works between the web and Android versions.
- Android: data stays on the phone only (no cloud sync yet). Claude indexing isn't available in the Android app yet.

## 6. Testing only (web version)
- ✅ Four example charts with made-up numbers (marked SAMPLE DATA) and one example link, added on first open of the web version only (never in the Android app). Flagged `example: true`.

## 7. Android app
- ✅ Capacitor wrapper around the same `index.html`, built on GitHub Actions and published as a GitHub release for tags `vX.Y.Z`.
- ✅ Updates keep your data: fixed app id `io.github.sergih123.facts`, one release key (`signing/`, password in a repo secret), versionCode derived from the version.
- ✅ Back button closes the top screen, then clears the search, then minimizes the app (so it reopens instantly).
- ✅ Own icon, dark launch screen, edge-to-edge safe areas.
- 🔜 On-device text reading (ML Kit) and Claude/API keywords.
- 🔜 Share target ("Share → Facts").
- 🔜 Cloud sync for the Android app.
