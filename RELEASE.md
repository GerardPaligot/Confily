# Release Guide

This project ships through two GitHub Actions workflows that build, sign,
and upload the apps to their respective stores:

- **`.github/workflows/cd-android.yaml`** — uploads a signed AAB to Google
  Play Console.
- **`.github/workflows/cd-ios.yaml`** — uploads a signed IPA to App Store
  Connect (build is then immediately available on TestFlight).

Both workflows are triggered manually (`workflow_dispatch`) so each fork
of this white-label app can release on its own cadence. Version
identifiers are passed as inputs, never committed to the repository.

The shared upload tooling is [Fastlane](https://fastlane.tools/), defined
in `fastlane/Fastfile` and `fastlane/Appfile`.

---

## Table of contents

1. [Prerequisites](#prerequisites)
2. [Configuring fork-specific identifiers](#configuring-fork-specific-identifiers)
3. [Firebase configuration: dev defaults vs prod override](#firebase-configuration-dev-defaults-vs-prod-override)
4. [Android secrets](#android-secrets)
5. [iOS secrets](#ios-secrets)
6. [Adding secrets to GitHub](#adding-secrets-to-github)
7. [Releasing](#releasing)
8. [Versioning strategy](#versioning-strategy)
9. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Android

- A Google Play Console account with the app already created and at least
  **one build manually uploaded to the Internal testing track**. Fastlane
  cannot bootstrap a brand-new app entry; it can only update an existing
  one.
- A **privacy policy URL** configured at **Play Console → your app →
  Policy → App content → Privacy policy**. Required because Confily
  declares sensitive runtime permissions (e.g. `CAMERA` for QR scanning);
  the Publishing API rejects uploads of any track — including internal —
  for apps with sensitive permissions when no policy URL is set.
- A Google Cloud project with the **Google Play Android Developer API**
  enabled (used to generate the service-account JSON).

### iOS

- An Apple Developer Program membership.
- An app entry created in **App Store Connect** with the bundle ID that
  matches your fork's `PRODUCT_BUNDLE_IDENTIFIER`.
- An **Apple Distribution** certificate created in your Apple Developer
  account.
- An **App Store** provisioning profile (not Ad Hoc / Development) created
  for that bundle ID and signed with the Distribution certificate.
- **Xcode 26 or later** active on the build machine. App Store Connect
  rejects every upload built with an earlier SDK (`409 SDK version
  issue`). The CD workflow pins
  `DEVELOPER_DIR=/Applications/Xcode_26.3.app/Contents/Developer` so the
  `macos-15` runner picks Xcode 26.3 (preinstalled alongside the default
  Xcode 16.4) instead of the default. When the runner image eventually
  ships a newer 26.x and retires 26.3, bump that path in
  `.github/workflows/cd-ios.yaml`.

---

## Configuring fork-specific identifiers

No fork-specific identifier is hardcoded in `fastlane/`. Each fork
configures its own values in two places:

### Android

The Android `applicationId` is sourced from `config/app.properties`
(or `config/app.properties.prod` if present). On CI, the workflow writes
`config/app.properties.prod` from the `APP_PROPERTIES` GitHub secret
before building. So for a fork:

1. Set `APPLICATION_ID=com.your.bundle.id` in your `APP_PROPERTIES`
   secret (see the Android section below).
2. The build and the Fastlane upload both read the same file — no
   further changes needed.

### iOS

iOS bundle ID and team ID are also driven from `config/app.properties`,
via the `iosApp/Scripts/generate_config.sh` script that produces
`iosApp/Generated.xcconfig` at build time. The Xcode project references
that xcconfig and pulls `$(IOS_BUNDLE_ID)` and `$(IOS_TEAM_ID)`
indirectly. So forks set:

```
IOS_BUNDLE_ID=com.your.bundle.id
IOS_TEAM_ID=ABCD123EFG
```

…in their `APP_PROPERTIES` secret. No `pbxproj` edits required.

Notes:

- The Debug variant uses `$(IOS_BUNDLE_ID).debug` automatically, so
  Debug and Release builds coexist on a single device.
- Team ID is the 10-character alphanumeric identifier from
  https://developer.apple.com/account → **Membership Details → Team ID**.
- Bundle ID must match what you registered at
  https://developer.apple.com/account → **Identifiers** *and* the App
  Store Connect app entry *and* the provisioning profile. All three
  must agree.
- Fastlane reads the bundle ID from the produced IPA at upload time, so
  no changes to `fastlane/` are required.

---

## Firebase configuration: dev defaults vs prod override

The repo commits a working `google-services.json` (phone and Wear) and
`GoogleService-Info.plist` so anyone — testers, contributors, fork
maintainers — can clone and build immediately. These are **dev / sample**
Firebase project files: real keys, but for a project whose telemetry the
upstream maintainer is happy to share with the world.

For production releases, the CD workflows optionally overwrite these
files from base64-encoded GitHub secrets just before the build step, then
restore the committed defaults during the cleanup step. This lets a fork
maintainer keep a separate production Firebase project (with prod
analytics, Crashlytics, FCM) without committing its config files.

The override is **optional**:

- If you set `GOOGLE_SERVICES_JSON_BASE64`, `GOOGLE_SERVICES_WEAR_JSON_BASE64`,
  or `GOOGLE_SERVICE_INFO_PLIST_BASE64` (described below), the CD run uses
  them.
- If you don't set them, the CD run packages the committed dev files —
  fine for forks that don't want to split dev and prod, or for an early
  release where you haven't created a separate prod Firebase project yet.

The override files must agree with the committed defaults on **package
name** (Android: `com.paligot.confily.android` and `…debug`; Wear:
`com.paligot.confily.wear`) and **bundle ID** (iOS: whatever you set via
`IOS_BUNDLE_ID`). Different Firebase projects, same identifiers — Firebase
allows it.

---

## Android secrets

Four GitHub secrets are required for the Android CD workflow, plus two
optional ones for the dev/prod Firebase split.

### `APP_PROPERTIES`

Full content of `config/app.properties.prod` for your fork. At minimum
it must contain `APPLICATION_ID`. This file is also used by other
workflows in the project (CI builds, lint, tests).

```sh
cat config/app.properties.prod | pbcopy
```

Paste as the secret value.

### `ANDROID_KEYSTORE_BASE64`

The release keystore file, base64-encoded.

If you don't have a release keystore yet:

```sh
keytool -genkey -v -keystore config/keystore.release \
  -alias confily -keyalg RSA -keysize 2048 -validity 10000
```

Note the password and key alias — you'll need them for
`ANDROID_KEYSTORE_PROPERTIES` below.

> **Important:** Once you upload an APK signed with this keystore to
> Play Console, **never lose it**. Play will not accept future updates
> signed with a different key. Back the file up to a password manager.

To generate the secret value:

```sh
base64 -i config/keystore.release | pbcopy
```

### `ANDROID_KEYSTORE_PROPERTIES`

Multi-line content of `config/keystore.properties`:

```
keyAlias=confily
keyPassword=<the keystore password>
```

```sh
cat config/keystore.properties | pbcopy
```

GitHub Secrets accept multi-line values.

### `PLAY_SERVICE_ACCOUNT_JSON`

Authorizes the workflow to call the Google Play Developer Publishing API.

**One-time service-account creation:**

1. Go to https://console.cloud.google.com → pick or create a project.
2. **APIs & Services → Library** → search "Google Play Android Developer
   API" → **Enable**.
3. **IAM & Admin → Service Accounts** → **Create Service Account**.
   - Name: e.g. `confily-play-publisher`.
   - Skip the "Grant access" step.
4. Open the new service account → **Keys** tab → **Add Key → Create new
   key → JSON**. A `.json` file downloads.

**Grant Play Console access:**

1. Go to https://play.google.com/console → **Users and permissions** →
   **Invite new users**.
2. Email = the service account's email (looks like
   `confily-play-publisher@your-project.iam.gserviceaccount.com`).
3. **App permissions** → add your app.
4. Permissions: **Release manager** (or scope tighter with "Release apps
   to testing tracks" + "Release to production…").
5. Send invitation. Propagation takes a few minutes.

**Stuff the JSON into the secret:**

```sh
cat ~/Downloads/your-project-XXXXX.json | pbcopy
```

Paste the entire JSON object including the curly braces.

### `GOOGLE_SERVICES_JSON_BASE64` *(optional)*

Base64-encoded `google-services.json` for your **production** phone-app
Firebase project. When set, the CD workflow overwrites the committed
`androidApp/google-services.json` for the duration of the build.

The file's `package_name` entries must match the dev default
(`com.paligot.confily.android` and `com.paligot.confily.android.debug`).

```sh
base64 -i androidApp/google-services.json | pbcopy
```

If unset, the CD run uses the committed dev file.

### `GOOGLE_SERVICES_WEAR_JSON_BASE64` *(optional)*

Same, for the Wear OS module's `wear/wear-app/google-services.json`. The
Wear app uses a different `package_name` (`com.paligot.confily.wear`)
and therefore a separate Firebase Android app entry.

```sh
base64 -i wear/wear-app/google-services.json | pbcopy
```

If unset, the CD run uses the committed dev file.

---

## iOS secrets

Seven GitHub secrets are required for the iOS CD workflow, plus one
optional one for the dev/prod Firebase split.

### `IOS_DISTRIBUTION_CERT_BASE64`

Your **Apple Distribution** certificate exported as a `.p12` file,
base64-encoded.

1. https://developer.apple.com/account → **Certificates, Identifiers &
   Profiles → Certificates** → **+** → **Apple Distribution** → Continue
   through the CSR flow.
2. Download the `.cer` file and double-click it to import into Keychain
   Access.
3. In Keychain Access, find the certificate (under **My Certificates**),
   right-click → **Export** → choose `.p12` format → set a password.
4. Generate the secret value:

   ```sh
   base64 -i ~/Downloads/dist.p12 | pbcopy
   ```

### `IOS_DISTRIBUTION_CERT_PASSWORD`

The password you typed when exporting the `.p12` above.

### `IOS_PROVISIONING_PROFILE_BASE64`

App Store provisioning profile for your bundle ID, base64-encoded.

1. https://developer.apple.com/account → **Profiles** → **+** →
   **App Store** → Continue.
2. Select your App ID (`com.your.bundle.id`).
3. Select your Distribution certificate.
4. Name the profile and download the `.mobileprovision` file.
5. Generate the secret value:

   ```sh
   base64 -i ~/Downloads/Confily_AppStore.mobileprovision | pbcopy
   ```

### `IOS_KEYCHAIN_PASSWORD`

An arbitrary password used to lock/unlock the temporary keychain that
the runner creates for the build. It is never stored or reused — any
random string works.

```sh
openssl rand -base64 32 | pbcopy
```

### `APP_STORE_CONNECT_KEY_ID`

The 10-character alphanumeric ID of your App Store Connect API key.

1. Go to https://appstoreconnect.apple.com → **Users and Access** →
   **Integrations** → **App Store Connect API** → **Keys**.
2. Click **+** to generate a new key.
   - Name: e.g. `Confily CI`.
   - Access: **App Manager** (sufficient for TestFlight uploads).
3. After creation, the **Key ID** is shown in the row. Copy that
   10-character string.

> **You can only download the `.p8` private-key file once**, immediately
> after creation. Save it before navigating away.

### `APP_STORE_CONNECT_ISSUER_ID`

The Issuer ID is a UUID shown above the keys list on the same page.

### `APP_STORE_CONNECT_KEY_BASE64`

The `.p8` private-key file you downloaded when creating the API key,
base64-encoded.

```sh
base64 -i ~/Downloads/AuthKey_XXXXXXXXXX.p8 | pbcopy
```

### `GOOGLE_SERVICE_INFO_PLIST_BASE64` *(optional)*

Base64-encoded `GoogleService-Info.plist` for your **production** iOS
Firebase project. When set, the CD workflow overwrites the committed
`iosApp/iosApp/GoogleService-Info.plist` for the duration of the build.

The file's `BUNDLE_ID` must match whatever you set via `IOS_BUNDLE_ID`
in `APP_PROPERTIES`.

```sh
base64 -i iosApp/iosApp/GoogleService-Info.plist | pbcopy
```

If unset, the CD run uses the committed dev file.

---

## Adding secrets to GitHub

For each secret listed above:

1. Go to your fork's **Settings → Secrets and variables → Actions**.
2. Click **New repository secret**.
3. Set the name to match exactly (e.g. `IOS_DISTRIBUTION_CERT_BASE64`).
4. Paste the value (multi-line is fine for `ANDROID_KEYSTORE_PROPERTIES`
   and the JSON / base64 secrets).
5. Save.

---

## Releasing

### Android

1. Go to **Actions → CD Android → Run workflow** (top right).
2. Fill in the inputs:

   | Input | Example | Notes |
   |---|---|---|
   | `version_name` | `4.0.2` | Shown to users in Play Store |
   | `version_code` | `40020` | Strictly greater than any previously published versionCode |
   | `track` | `internal` | Default; promotion to other tracks happens via Play Console (or re-run with `production`) |
   | `release_status` | `draft` | Default; uploaded build sits as a Draft awaiting manual rollout in Play Console. Switch to `completed` once your app is out of Play Console's draft state and you trust the workflow to auto-roll-out. |
   | `tag_release` | `true` | Creates a `vX.Y.Z` git tag on the build commit |

3. Click **Run workflow**. The workflow takes ~5–10 minutes.
4. The build appears in **Play Console → your app → Release → Testing →
   Internal testing**. With `release_status: draft`, click **Review
   release → Start rollout** to publish to your testers.

### iOS

1. Go to **Actions → CD iOS → Run workflow** (top right).
2. Fill in the inputs:

   | Input | Example | Notes |
   |---|---|---|
   | `version_name` | `4.0.2` | CFBundleShortVersionString |
   | `version_code` | `40020` | CFBundleVersion — strictly greater than any previously uploaded build for that bundle ID |
   | `tag_release` | `true` | Creates a `vX.Y.Z-ios` git tag on the build commit |

3. Click **Run workflow**. The workflow takes ~10–20 minutes
   (most of the time is in the Xcode archive step).
4. The build appears in **App Store Connect → your app → TestFlight →
   Builds** after Apple's processing finishes (5–30 min). Distribution
   to testers and App Store submission stay manual via App Store Connect.

---

## Versioning strategy

Both platforms decouple the **version name** (user-visible) from the
**version code** (monotonic integer). Each fork manages its own
versioning history, so neither value is committed to the repository.

The committed `config/version.properties` is a baseline (`1.0.0`) used
only for local development builds. CI overrides it via environment
variables (`CI_VERSION_NAME`, `CI_VERSION_CODE`) read by
`AndroidApplicationPlugin.kt`, and via `xcargs` on iOS.

A common scheme that gives plenty of headroom is:

```
versionCode = major * 10000 + minor * 100 + patch
```

So `4.0.2` → `40002`, `4.1.0` → `40100`. Each fork should pick a
scheme and stick with it; document yours in your fork's README.

### Tag scheme

By default the workflows create:

- **Android:** `vX.Y.Z` (e.g. `v4.0.2`)
- **iOS:** `vX.Y.Z-ios` (e.g. `v4.0.2-ios`)

The iOS suffix prevents tag collisions when both platforms ship the
same version on the same commit. Set `tag_release` to `false` at
trigger time if you don't want a tag.

---

## Troubleshooting

### Android: "Package not found: com.your.bundle.id"

You haven't uploaded a build to Play Console yet. Fastlane requires an
existing app entry; it cannot bootstrap one. Build and upload an AAB
manually via Play Console once, then re-run the workflow.

### Android: "The apk has permissions that require a privacy policy set for the app"

Confily declares sensitive runtime permissions (e.g. `CAMERA` for QR
scanning) and Google Play rejects API uploads of any track when no
privacy policy URL is configured. Set one at **Play Console → your app
→ Policy → App content → Privacy policy → Manage** and re-run the
workflow. The check is server-side; there is no client flag to skip it.

### Android: "Only releases with status draft may be created on draft app"

Your Play Console app is still in draft state — it has a listing but
has never had a release manually published to any track. Two options:

- Run the workflow with `release_status: draft` (the default), upload
  the build, then in Play Console finish any incomplete sections under
  **Dashboard → Set up your app** and roll out the draft release
  manually. After the first manual rollout, the app exits draft state
  and future runs can use `release_status: completed`.
- Skip the API entirely for the very first release — upload an AAB
  manually via the Play Console UI to clear the draft state, then use
  the workflow for all subsequent releases.

### Android: "Version code X has already been used"

The `version_code` input is less than or equal to a previously uploaded
build. Increment past the highest historical value (visible in Play
Console → Bundles).

### iOS: "No certificate matching … was found in keychain"

The `IOS_DISTRIBUTION_CERT_BASE64` and the
`IOS_PROVISIONING_PROFILE_BASE64` don't match. The provisioning profile
embeds a reference to the specific Distribution certificate that signed
it; if you regenerated the certificate, regenerate the profile too.

### iOS: build hangs or times out at the codesign step

Usually means the keychain partition list is misconfigured. The workflow
runs `security set-key-partition-list` to grant `codesign` non-
interactive access — confirm `IOS_KEYCHAIN_PASSWORD` is set and matches
across the import / unlock / partition-list steps.

### iOS: "Invalid provisioning profile" on App Store Connect

The profile expired (Apple rotates them yearly) or the bundle ID in
`pbxproj` doesn't match the profile. Regenerate at
https://developer.apple.com/account → Profiles.

### iOS: "Validation failed (409) SDK version issue"

App Store Connect requires every upload to be built with the iOS 26 SDK
or later (Xcode 26+). The archive itself succeeds — the rejection comes
from `altool` at upload time:

```
This app was built with the iOS 18.5 SDK. All iOS and iPadOS apps must
be built with the iOS 26 SDK or later, included in Xcode 26 or later,
in order to be uploaded to App Store Connect or submitted for
distribution.
```

The workflow pins
`DEVELOPER_DIR=/Applications/Xcode_26.3.app/Contents/Developer` on the
`macos-15` job so the runner uses Xcode 26.3 instead of the default
16.4. If that override was removed, or the runner image stopped shipping
Xcode 26.3 (later 26.x typically replace it in place), the archive falls
back to an older SDK and Apple rejects the IPA. Restore or update the
`DEVELOPER_DIR` line in the job-level `env:` block of
`.github/workflows/cd-ios.yaml` to whatever Xcode 26+ path the runner
image provides.

### Workflow is missing a required secret

The "Verify deploy secrets" step fails fast with the missing variable
name. Add the secret in **Settings → Secrets and variables → Actions**
and re-run the workflow.

### First successful CI run produces a `Gemfile.lock`

You can either commit it (recommended for reproducible runs) by
downloading it from the workflow's working directory, or run
`bundle install` locally with Ruby 3.0+ and commit the result.

---

## Local testing

Fastlane lanes can be run locally before relying on CI:

```sh
# Android (requires config/keystore.release, config/keystore.properties,
# config/play-service-account.json, and a built AAB in
# androidApp/build/outputs/bundle/release/)
bundle install
./gradlew :androidApp:bundleRelease
bundle exec fastlane android deploy track:internal

# iOS (requires Xcode 26 or later active — App Store Connect rejects
# uploads built with earlier SDKs. Either set it as your default via
# `sudo xcode-select -s /Applications/Xcode_26.app/Contents/Developer`
# or prefix the lane with DEVELOPER_DIR=… ; also requires the
# distribution certificate already in your login keychain, the
# provisioning profile installed, and config/app_store_connect_key.p8
# present)
bundle exec fastlane ios deploy version_name:1.0.0 version_code:1
```

Local runs against the production Play Store / App Store will publish
real builds — use them with care.
