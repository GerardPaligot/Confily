---
name: kotlin-linting
description: "Use this skill whenever you write or modify Kotlin files in this project. Covers the ktlint and detekt rules enforced in CI (./gradlew ktlintCheck detekt) and the most common mistakes to avoid: wildcard imports, long parameter lists, too many functions, complex methods, and magic numbers. Always apply these rules before committing."
---

# Kotlin Linting Skill

## CI Enforcement

The linter job in `.github/workflows/build.yaml` runs:

```
./gradlew ktlintCheck detekt
```

Both tools run with `maxIssues: 0` / `ignoreFailures: false`, so **a single violation breaks the build**. Fix issues before committing.

## ktlint Rules

ktlint version `0.47.1` is used with experimental rules enabled. The only disabled rule is `experimental:argument-list-wrapping`.

### Wildcard imports are forbidden

Always use fully qualified imports. Never use `*`:

```kotlin
// ❌ forbidden
import io.ktor.http.*
import io.ktor.server.routing.*

// ✅ correct
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
```

### Generated and build directories are excluded

Files under `/generated/` or `/build/` are ignored automatically. Only `**/kotlin/**` sources are linted.

## detekt Rules

Configuration lives in `config/detekt/detekt.yml`.

### Key thresholds

| Rule | Threshold |
|---|---|
| `ComplexMethod` | 15 cyclomatic complexity |
| `LongMethod` | 100 lines |
| `LongParameterList` (functions) | 6 parameters |
| `LongParameterList` (constructors) | 7 parameters |
| `TooManyFunctions` | 12 per file / class / object |
| `LargeClass` | 600 lines |
| `NestedBlockDepth` | 4 levels |
| `ComplexCondition` | 4 conditions |

`LongParameterList` is ignored for data classes and for functions/classes annotated with `@Composable`, `@Stable`, or `@Immutable`.

### Suppression annotations

When a threshold genuinely cannot be avoided, suppress at the declaration level with a `@Suppress` annotation and a comment explaining why:

```kotlin
@Suppress("LongParameterList") // DI constructor — all params are required
class MyRepository(...)
```

Prefer refactoring over suppression wherever possible.

### Test sources are excluded

The rules below are **not** enforced in test source sets (`**/test/**`, `**/androidTest/**`, `**/commonTest/**`, etc.):

- `WildcardImport` (via ktlint, but keep imports explicit in tests too)
- `StringLiteralDuplication`
- `TooManyFunctions`
- `LongParameterList`

### Empty blocks

All empty block rules are active. Do not leave empty `catch`, `if`, `else`, or function bodies without a comment or intentional content.

## Running Locally

```bash
# Check only
./gradlew ktlintCheck detekt

# Auto-fix ktlint issues
./gradlew ktlintFormat

# detekt cannot auto-fix — review the HTML report at build/reports/detekt/
```
