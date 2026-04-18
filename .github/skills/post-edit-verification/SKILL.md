---
name: post-edit-verification
description: "Use this skill after every working session where Kotlin or Android code was created or modified. It ensures the project compiles and passes lint checks before considering work complete. Run the verification commands below and fix any issues before committing."
---

# Post-Edit Verification Skill

## When to Use

Run these checks **after every working session** that touches Kotlin, Android, or shared module code — before committing or reporting work as done.

## Compilation Check

Compile the modules you modified. Target the specific Gradle tasks for faster feedback:

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

## Lint Check

Run both ktlint and detekt. Both must pass with zero violations:

```bash
./gradlew ktlintCheck detekt
```

See the `kotlin-linting` skill for rules and thresholds.

## Common Issues

| Issue | Fix |
|---|---|
| Import ordering (ktlint) | Imports must be lexicographically sorted with no blank lines between them. `java`, `javax`, `kotlin` and aliases go last. |
| Missing private helper functions | If you reference a function in a composable, make sure it exists in the same file or is imported. |
| Wildcard imports | Always use explicit imports. Never use `*`. |
| Duplicate imports | When adding a new import, check it isn't already present elsewhere in the import block. Place it in the correct alphabetical position. |

## Checklist

1. Compile modified modules — `BUILD SUCCESSFUL`
2. Run `./gradlew ktlintCheck detekt` — `BUILD SUCCESSFUL`
3. Fix any errors and re-run until clean
4. Then commit
