# Facts

A fast personal archive of charts, graphs, screenshots and links for backing up points in arguments. Open it, type, and find the chart.

- `index.html`: the whole app. The same file runs as the web version (claude.ai artifact) and inside the Android app.
- `REQUIREMENTS.md`: feature spec.

## Android app

Built entirely on GitHub Actions (`.github/workflows/android.yml`) with Capacitor:

1. `npm run android` builds `www/` from `index.html`, generates `android/` (not committed) and customizes it with `scripts/prepare-android.py` (icon, keyboard on launch, versioning, signing).
2. Gradle builds a release APK signed with `signing/facts-release.p12`. The password lives only in the repo secret `FACTS_KEYSTORE_PASSWORD`.
3. Pushing a tag `vX.Y.Z` publishes `Facts-X.Y.Z.apk` as a GitHub release. versionCode = X*10000 + Y*100 + Z.

**Never change** the app id (`io.github.sergih123.facts`), the keystore, or Capacitor's `androidScheme`/hostname. Changing any of them means installed apps can't update or lose their data.
