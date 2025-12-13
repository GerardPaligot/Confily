# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]
**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

[Extract from feature spec: primary requirement + technical approach from research]

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Kotlin 2.0+, Swift 5.9+
**Primary Dependencies**: Jetpack Compose, Compose Multiplatform, SwiftUI, Koin, Ktor, SQLDelight
**Storage**: SQLDelight (local), Firestore (backend), Cloud Storage (media)
**Testing**: Kotlin Test, JUnit
**Target Platform**: Android API 26+, iOS 15+, Desktop (JVM), Backend (App Engine)
**Project Type**: Kotlin Multiplatform (mobile + backend)
**Performance Goals**: <2s cold start, 60fps UI, <150MB memory (phone)
**Constraints**: Offline-first, low-bandwidth resilient, accessible across platforms
**Scale/Scope**: Conference attendees (100-5000 users/event), multi-event support

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

**I. Modular Feature Architecture**
- [ ] Feature follows `feature-name/{di, panes, presentation, routes, ui, ui-models, semantics}` structure
- [ ] Shared functionality uses `shared/core/` modules appropriately
- [ ] Platform-specific code is properly isolated (no cross-contamination)
- [ ] Module dependencies are clearly defined and unidirectional

**II. Comprehensive Testing Standards**
- [ ] Test modules planned (`feature-name-test`)
- [ ] Unit test coverage planned for business logic
- [ ] UI semantics defined for accessibility/testability
- [ ] Integration test scenarios identified for cross-module interactions

**III. Cross-Platform UX Consistency**
- [ ] Design system components from `style/` modules will be used
- [ ] Adaptive layouts planned for multiple screen sizes
- [ ] Accessibility requirements identified (semantic labels, screen reader support)
- [ ] Platform-specific adaptations justified and documented

**IV. Performance & Resource Optimization**
- [ ] Performance targets identified (startup time, memory, network)
- [ ] Image loading strategy defined (Coil configuration)
- [ ] Background work uses Kotlin Coroutines with lifecycle management
- [ ] Offline/caching strategy defined (SQLDelight integration)

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths. The delivered plan must not include Option labels.
-->

```text
# Confily: Kotlin Multiplatform with modular features
features/
└── [feature-name]/
    ├── [feature-name]-di/           # Dependency injection module
    ├── [feature-name]-panes/        # Pane composables for adaptive layouts
    ├── [feature-name]-presentation/ # ViewModels and business logic
    ├── [feature-name]-routes/       # Navigation routes
    ├── [feature-name]-semantics/    # UI semantics for testing/accessibility
    ├── [feature-name]-test/         # Feature tests
    ├── [feature-name]-ui/           # UI components
    └── [feature-name]-ui-models/    # UI state models

shared/
├── core/            # Shared cross-platform code
├── core-api/        # API client interfaces
├── core-db/         # SQLDelight database
├── core-di/         # Koin DI configuration
├── core-navigation/ # Navigation framework
└── models/          # Shared data models

backend/
└── src/
    ├── main/        # Ktor server, Firestore integration
    └── test/        # Backend tests

composeApp/
└── src/
    ├── androidMain/   # Android-specific code
    ├── desktopMain/   # Desktop-specific code
    └── commonMain/    # Shared Compose Multiplatform code

iosApp/
└── iosApp/          # SwiftUI iOS application

style/
├── components/      # Shared design system components
├── theme/           # Theming (colors, typography, spacing)
└── [feature]/       # Feature-specific styles

wear/
└── wear-app/        # Android Wear OS companion app
```

**Structure Decision**: [Document how this feature fits into the modular structure above and which modules will be created/modified]

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |
