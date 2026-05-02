---
name: post-edit-verification
description: "Use this skill after every working session where Kotlin or Android code was created or modified, and ALWAYS before committing, opening a PR, or reporting work as done. Runs the same three quality gates CI runs — ktlint, detekt, and Android lint — so failures are caught locally instead of by GitHub Actions."
---

# Post-Edit Verification Skill

## When to Use

Run these checks **after every working session** that touches Kotlin, Android, or shared module code — before committing, before opening a PR, before reporting work as done. CI fails on any of the three checks below; run them locally to avoid the round-trip.

## CI Reference

`.github/workflows/build.yaml` (job `linter`) runs exactly two commands. Both must succeed:

```bash
./gradlew ktlintCheck detekt
./gradlew lintDebug
```

Mirror these locally — do not skip Android lint just because ktlint and detekt passed. They catch different classes of issues (Android lint covers `MissingPermission`, `UnusedResources`, manifest checks, etc. that ktlint and detekt don't).

## Compilation Check

Compile the modules you modified. Target specific Gradle tasks for faster feedback:

```bash
# Android modules
./gradlew :<module-path>:compileDebugKotlinAndroid

# iOS shared modules
./gradlew :<module-path>:compileKotlinIosArm64

# Full Android app assembly (slower, use when unsure which modules are affected)
./gradlew :composeApp:assembleDebug
```

Examples for common modules:

```bash
./gradlew :features:schedules:schedules-panes:compileDebugKotlinAndroid
./gradlew :features:navigation:compileDebugKotlinAndroid
./gradlew :shared:core:compileKotlinIosArm64
```

## Quality Gates

All three must pass with zero violations.

### 1. ktlint + detekt

```bash
./gradlew ktlintCheck detekt
```

See the `kotlin-linting` skill for rules and thresholds. Auto-fix ktlint issues with `./gradlew ktlintFormat`. Detekt cannot auto-fix — review `build/reports/detekt/`.

### 2. Android lint

```bash
./gradlew lintDebug
```

Android lint runs on all Android-targeting modules. Fail-fast scope: `:androidApp:lintDebug` is enough if you only touched the app module, but the top-level `lintDebug` matches CI exactly. Reports land in `androidApp/build/reports/lint-results-debug.html`.

Android lint catches issues the Kotlin tools don't:
- `MissingPermission` — calls that need a runtime permission check
- `UnusedResources`, `IconMissingDensityFolder`, etc. — resource hygiene
- Manifest issues — `exported` flags, intent filters, deprecated APIs
- API-level usage — calls to APIs above `minSdk` without a `Build.VERSION.SDK_INT` guard

When suppression is genuinely warranted, use `@SuppressLint("RuleId")` at the smallest possible scope and add a one-line comment explaining why the static check is wrong (e.g., the runtime check is stronger but lint can't see it).

## Common Issues

| Issue | Tool | Fix |
|---|---|---|
| Import ordering | ktlint | Imports must be lexicographically sorted with no blank lines between them. `java`, `javax`, `kotlin` and aliases go last. |
| Wildcard imports | ktlint | Always use explicit imports. Never use `*`. |
| Duplicate imports | ktlint | When adding a new import, check it isn't already present elsewhere in the import block. Place it in the correct alphabetical position. |
| `ReturnCount` (>2) | detekt | Collapse early returns into a single guard, or extract a helper function. |
| `ComplexCondition` (>3 boolean ops) | detekt | Break the condition apart, or extract validation into a helper. |
| `MissingPermission` on `notify()`, location calls, etc. | Android lint | Call `ContextCompat.checkSelfPermission` (or an equivalent gate like `NotificationManagerCompat.areNotificationsEnabled()`); if the gate is semantically stronger but lint can't see it, suppress at the function with a justifying comment. |
| Missing private helper functions | compile | If you reference a function in a composable, make sure it exists in the same file or is imported. |

## Checklist

1. Compile modified modules — `BUILD SUCCESSFUL`
2. `./gradlew ktlintCheck detekt` — `BUILD SUCCESSFUL`
3. `./gradlew lintDebug` — `BUILD SUCCESSFUL`
4. Fix any errors and re-run until all three are clean
5. Then commit / open the PR
