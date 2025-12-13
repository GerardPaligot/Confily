<!--
SYNC IMPACT REPORT
==================
Version Change: N/A → 1.0.0
Rationale: Initial constitution creation for Confily project

Added Principles:
- I. Modular Feature Architecture (Code Quality)
- II. Comprehensive Testing Standards (Testing)
- III. Cross-Platform UX Consistency (User Experience)
- IV. Performance & Resource Optimization (Performance)

Added Sections:
- Technology Stack Standards
- Development Workflow & Quality Gates

Templates Status:
- ✅ plan-template.md: Updated with constitution checks, Confily-specific technical context, and modular structure
- ✅ spec-template.md: Verified - user story prioritization supports modular development
- ✅ tasks-template.md: Verified - task organization by user story supports independent testing
- ✅ checklist-template.md: Verified - checklist format is flexible and compatible

Follow-up TODOs: None
-->

# Confily Constitution

## Core Principles

### I. Modular Feature Architecture

Every feature MUST be developed as an independent, self-contained module following the established directory structure:

- **Module Organization**: Features follow `feature-name/{di, panes, presentation, routes, ui, ui-models, semantics}` structure
- **Clear Separation**: Presentation logic, UI components, dependency injection, and routing MUST be separated into distinct modules
- **Shared Core**: Common functionality MUST reside in `shared/core/` modules (api, db, di, fs, navigation, models, resources)
- **Platform-Specific Isolation**: Android (`androidMain`), iOS (Swift), Desktop (`desktopMain`), and backend code MUST NOT cross-contaminate
- **Module Independence**: Each module MUST be independently buildable and testable

**Rationale**: Modular architecture ensures scalability, enables parallel development, simplifies testing, and maintains clear boundaries in a complex multiplatform codebase.

### II. Comprehensive Testing Standards

Testing is NON-NEGOTIABLE and MUST follow these requirements:

- **Test Module Structure**: Every feature MUST have corresponding test modules (`feature-name-test`, `core-test`, `core-test-patterns`)
- **Unit Tests**: All business logic in presentation and service layers MUST have unit tests
- **UI Tests**: UI components MUST have semantics modules for accessibility and testability
- **Integration Tests**: Cross-module interactions, database operations, and API contracts MUST have integration tests
- **Platform Coverage**: Tests MUST cover Android, iOS (where applicable), and backend independently
- **Test-First Development**: For critical features, write failing tests before implementation

**Rationale**: Comprehensive testing prevents regressions, ensures cross-platform parity, validates modular contracts, and maintains confidence in a rapidly evolving multiplatform project.

### III. Cross-Platform UX Consistency

User experience MUST be consistent across all platforms while respecting platform conventions:

- **Design System**: UI components in `style/` modules MUST be used consistently (components, theme, events, networking, partners, schedules, speakers)
- **Compose Multiplatform**: Shared UI logic MUST use Compose Multiplatform where feasible
- **Platform Adaptation**: iOS (SwiftUI) and Android (Jetpack Compose) MAY diverge for platform-specific interactions but MUST maintain functional parity
- **Adaptive Layouts**: MUST support multiple screen sizes (phone, tablet, desktop, wearable) using Adaptive Layout guidelines
- **Accessibility**: MUST include semantic labels, keyboard navigation, and screen reader support across platforms
- **Visual Consistency**: Color schemes, typography, spacing, and component behavior MUST align across platforms

**Rationale**: Consistent UX builds user trust, reduces cognitive load, and ensures feature parity across Android, iOS, desktop, and wearable apps.

### IV. Performance & Resource Optimization

Performance is a first-class requirement. MUST meet these standards:

- **App Startup**: Cold start MUST be < 2 seconds on mid-range devices (Android API 26+, iOS 15+)
- **Memory Budget**: App MUST stay within 150MB memory on phones, 250MB on tablets under normal usage
- **Network Efficiency**: API calls MUST use caching (SQLDelight), minimize payloads, and handle offline scenarios gracefully
- **UI Responsiveness**: UI MUST maintain 60fps during scrolling, animations, and interactions
- **Image Optimization**: Coil MUST be configured with proper memory caching and downsampling for conference schedules/speaker images
- **Baseline Profiles**: Android MUST use baseline profiles (`baselineprofile/`) to optimize startup and reduce jank
- **Background Work**: Long-running operations MUST use Kotlin Coroutines with proper lifecycle management
- **Wearable Constraints**: Wearable app MUST be optimized for limited screen space and battery life

**Rationale**: Performance directly impacts user satisfaction and app store ratings. Conference apps are used in low-bandwidth environments (conference WiFi) and must remain responsive.

## Technology Stack Standards

All development MUST adhere to these stack requirements:

- **Languages**: Kotlin (Android/Backend/Shared), Swift (iOS native)
- **UI Frameworks**: Jetpack Compose (Android), Compose Multiplatform (Shared UI), SwiftUI (iOS)
- **Dependency Injection**: Koin for all platforms
- **Database**: SQLDelight for type-safe SQL on all platforms
- **Networking**: Ktor client (mobile), Ktor server (backend)
- **Backend Services**: Firestore (database), Cloud Storage (media), App Engine (hosting)
- **Serialization**: Kotlinx Serialization for JSON/data serialization
- **Navigation**: Navigation Compose (Android/Desktop), native navigation (iOS)
- **Image Loading**: Coil (Android/Desktop)
- **Coroutines**: Kotlinx Coroutines for asynchronous operations
- **Build System**: Gradle with Kotlin DSL, convention plugins in `build-logic/`
- **Code Quality**: Detekt for static analysis (`config/detekt/detekt.yml`)
- **Configuration**: BuildKonfig for build config, Multiplatform Settings for preferences

**Prohibited**: Adding new libraries without justification; Using deprecated APIs; Mixing serialization libraries; Platform-specific code in shared modules

## Development Workflow & Quality Gates

All code changes MUST pass these quality gates:

### Code Review Requirements
- All PRs MUST be reviewed by at least one maintainer
- PRs MUST include tests for new features or bug fixes
- PRs MUST update documentation if public APIs change
- PRs MUST pass all CI checks (build, lint, test)

### Quality Checks
- **Linting**: Detekt MUST pass with zero violations
- **Build**: All modules MUST compile on all platforms
- **Tests**: All existing tests MUST pass; New code MUST add tests
- **Performance**: Baseline profiles MUST be regenerated for significant UI changes

### Branching Strategy
- Feature branches: `feature/###-feature-name`
- Bugfix branches: `fix/###-bug-description`
- Release branches: `release/vX.Y.Z`
- Main branch MUST always be deployable

### Documentation Requirements
- Architecture decisions MUST be documented in `/specs/` using the spec kit templates
- API changes MUST update relevant documentation in `shared/` modules

## Governance

This constitution supersedes all other development practices. All team members, contributors, and AI agents MUST verify compliance during development.

### Amendment Process
- Amendments require documented rationale and team consensus
- Version MUST be incremented using semantic versioning (MAJOR.MINOR.PATCH)
- All dependent templates and documentation MUST be updated within the same PR

### Compliance
- All PRs MUST verify alignment with core principles
- Violations MUST be justified in PR description and approved by maintainers
- Templates in `.specify/templates/` provide detailed guidance for feature development

### Version Control
- **MAJOR**: Breaking changes to architecture, removing principles, or incompatible governance changes
- **MINOR**: Adding new principles, expanding guidance, or new quality gates
- **PATCH**: Clarifications, typo fixes, or non-semantic improvements

**Version**: 1.0.0 | **Ratified**: 2025-12-13 | **Last Amended**: 2025-12-13
