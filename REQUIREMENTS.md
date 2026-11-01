# Facts: requirements

A personal archive of charts, graphs, screenshots and links for quickly backing up points while arguing in person. **Speed is the top priority.**

Status: ✅ built in the web version · 🔜 planned for native

## 1. Launch and search
- ✅ The app opens straight into search, with the search box focused.
- 🔜 Native: the keyboard opens automatically on launch, which a web page can't force.
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
- 🔜 Export/import a backup (needed to move data from the web version into the native app).

## 6. Testing only (remove before production)
- ✅ Four example charts with made-up numbers (marked SAMPLE DATA) and one example link, added automatically on first open. Flagged `example: true` in code and data.

## 7. Native Android plan
- Wrap the same web code with Capacitor, so every feature above carries over without rewriting.
- Replace the claude.ai storage, file upload and Claude calls with a native or own backend: SQLite or IndexedDB locally, plus optional cloud sync.
- Build the APK with GitHub Actions.
- Rewrite screens in Flutter or Kotlin only if something still feels slow.
