# Implementation Plan: Postgres Repository Implementation with Kotlin Exposed

**Branch**: `001-firestore-postgres-migration` | **Date**: December 13, 2025 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-firestore-postgres-migration/spec.md`

**Note**: This plan documents the implementation of Postgres repositories using Kotlin Exposed to achieve feature parity with existing Firestore repositories.

## Summary

Create Postgres repository implementations for all 16 entity types (Activity, Address, Category, Event, Format, Job, LunchMenu, Map, Partner, QAndA, Schedule, Session, Social, Speaker, Tag, Team) using Kotlin Exposed library, resulting in 31 total tables including normalization tables and junction tables. Each entity will have a corresponding `{Entity}RepositoryExposed` class that implements the same domain repository interface as the Firestore version, ensuring complete API compatibility. Implementation will proceed entity-by-entity in 4 phases, starting with core entities (Event, Category, Format) and progressing to more complex nested structures. Normalized table schemas with foreign key constraints will enforce data integrity, and junction tables with composite primary keys will manage many-to-many relationships. Final schema includes 59 indexes, 42 foreign keys, and 5 junction tables for many-to-many relationships.

## Technical Context

**Language/Version**: Kotlin 2.0+ (backend module)
**Primary Dependencies**: 
- Kotlin Exposed 0.50+ (SQL DSL, schema migration via SchemaUtils, connection pooling)
- PostgreSQL JDBC Driver 42.7+
- Ktor Server 2.3+ (existing backend framework)
- Kotlinx Serialization (existing data serialization)

**Storage**: 
- Current: Firestore (document database) in existing cloud infrastructure
- Target: PostgreSQL 14+ (relational database) in separate cloud infrastructure
- Local Development: PostgreSQL via Docker Compose

**Testing**: 
- JUnit 5 for all tests (unit and integration)
- Ktor Test Client for HTTP API integration tests
- H2 in-memory database for fast integration tests
- Existing Firestore repository tests as behavioral reference

**Target Platform**: Backend (JVM/Ktor Server on App Engine or equivalent)

**Performance Goals**: 
- Query performance ≤ 200ms for 95% of requests (match or exceed Firestore)
- Support 1000+ concurrent requests without degradation
- Handle 10,000+ records per entity type

**Constraints**: 
- Must maintain existing domain repository interfaces (zero API contract changes)
- Fresh Postgres deployment (no live data migration from production Firestore)
- Entity-by-entity implementation for independent validation
- RESTRICT cascade behavior for foreign keys (prevent accidental deletion)
- Last-write-wins concurrency (no optimistic locking)
- Normalized schema design (no JSONB columns for nested data)

**Scale/Scope**: 
- 16 entity types across backend module
- Support for multiple events with 100-5000 attendees each
- Sessions (500+ per event), Speakers (200+ per event), Partners (50+ per event)
- Conference management operations (CRUD + complex queries with filters/sorting)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

**I. Modular Feature Architecture**
- [x] Feature follows existing backend module structure (`backend/src/main/java/com/paligot/confily/backend/{entity}/`)
- [x] Shared functionality uses existing infrastructure package (`internals/infrastructure/`)
- [x] Platform-specific code is properly isolated (backend-only, no mobile cross-contamination)
- [x] Module dependencies are clearly defined (Exposed repositories implement existing domain interfaces)

**Justification**: This is a backend-only change. The existing Firestore repositories are in `internals/infrastructure/firestore/`, and new Exposed repositories will be in `internals/infrastructure/exposed/` (or equivalent). Each entity domain already has clear interfaces that both implementations will honor.

**II. Comprehensive Testing Standards**
- [x] Test modules planned (integration tests with H2 in-memory database)
- [x] Unit test coverage planned for repository implementations (CRUD operations, queries)
- [x] UI semantics defined for accessibility/testability (N/A - backend only)
- [x] Integration test scenarios identified (test parity with Firestore behavior)

**Justification**: Each Exposed repository will have comprehensive tests comparing behavior to Firestore repositories. Integration tests use H2 in-memory database with Ktor Test Client for fast, isolated testing without Docker dependencies.

**III. Cross-Platform UX Consistency**
- [x] Design system components from `style/` modules will be used (N/A - backend only)
- [x] Adaptive layouts planned for multiple screen sizes (N/A - backend only)
- [x] Accessibility requirements identified (N/A - backend only)
- [x] Platform-specific adaptations justified (Backend only - no frontend changes)

**Justification**: This is a backend infrastructure change with zero client-facing impact. API contracts remain unchanged, ensuring consistent UX across all platforms.

**IV. Performance & Resource Optimization**
- [x] Performance targets identified (<200ms queries, 1000+ concurrent requests, 10,000+ records)
- [x] Image loading strategy defined (N/A - backend storage only, no image processing)
- [x] Background work uses Kotlin Coroutines with lifecycle management (Exposed integrates with suspend functions)
- [x] Offline/caching strategy defined (N/A - backend is always online; clients use existing caching)

**Justification**: Performance targets align with constitution requirements. Exposed's built-in connection pooling and proper indexing will ensure responsive queries. Kotlin Coroutines integration with Exposed provides efficient async I/O.

## Project Structure

### Documentation (this feature)

```text
specs/001-firestore-postgres-migration/
├── plan.md              # This file
├── research.md          # Phase 0 output (Exposed best practices, schema design patterns) ✅
├── data-model.md        # Phase 1 output (Entity schemas, relationships, indexes) ✅
├── quickstart.md        # Phase 1 output (Local dev setup, Postgres + Exposed guide) ✅
├── contracts/           # Phase 1 output (Exposed Table definitions + reference SQL) ✅
│   ├── README.md        # Schema management overview and usage guide
│   ├── V1__create_events_table.sql         # Phase 1 Foundation (reference SQL)
│   ├── V1a__create_event_features_table.sql # Phase 1 Normalization (reference SQL)
│   ├── V1b__create_event_session_tracks_table.sql # Phase 1 Normalization (reference SQL)
│   ├── V1c__create_team_groups_table.sql   # Phase 1 Normalization (reference SQL)
│   ├── V1d__create_sponsoring_types_table.sql # Phase 1 Normalization (reference SQL)
│   ├── V2__create_categories_table.sql     # Phase 1 Foundation (reference SQL)
│   ├── V3__create_formats_table.sql        # Phase 1 Foundation (reference SQL)
│   ├── V4__create_speakers_table.sql       # Phase 2 Content (reference SQL)
│   ├── V5__create_tags_table.sql           # Phase 2 Content (reference SQL)
│   ├── V6__create_sessions_table.sql       # Phase 2 Content (reference SQL)
│   ├── V6a__create_session_categories_table.sql  # Phase 2 Junction (reference SQL)
│   ├── V7__create_session_speakers_table.sql  # Phase 2 Junction (reference SQL)
│   ├── V8__create_session_tags_table.sql      # Phase 2 Junction (reference SQL)
│   ├── V8a__create_event_sessions_table.sql   # Phase 2 Content (reference SQL)
│   ├── V9__create_partners_table.sql          # Phase 3 Extended (reference SQL)
│   ├── V9a__create_partner_sponsorships_table.sql  # Phase 3 Junction (reference SQL)
│   ├── V10__create_team_table.sql          # Phase 3 Extended (reference SQL)
│   ├── V11__create_socials_table.sql          # Phase 3 Extended (reference SQL)
│   ├── V11a__create_speaker_socials_table.sql # Phase 3 Junction (reference SQL)
│   ├── V11b__create_partner_socials_table.sql # Phase 3 Junction (reference SQL)
│   ├── V12__create_jobs_table.sql             # Phase 3 Extended (reference SQL)
│   ├── V13__create_activities_table.sql       # Phase 4 Specialized (reference SQL)
│   ├── V14__create_schedules_table.sql        # Phase 4 Specialized (reference SQL)
│   ├── V15__create_maps_table.sql             # Phase 4 Specialized (reference SQL)
│   ├── V15a__create_map_shapes_table.sql      # Phase 4 Specialized (reference SQL)
│   ├── V15b__create_map_pictograms_table.sql  # Phase 4 Specialized (reference SQL)
│   ├── V16__create_lunch_menus_table.sql      # Phase 4 Specialized (reference SQL)
│   ├── V17__create_qanda_table.sql            # Phase 4 Specialized (reference SQL)
│   ├── V17a__create_qanda_actions_table.sql   # Phase 4 Specialized (reference SQL)
│   ├── V17b__create_qanda_acronyms_table.sql  # Phase 4 Specialized (reference SQL)
│   └── V18__create_addresses_table.sql        # Phase 4 Specialized (reference SQL)
└── tasks.md             # Phase 2 output (NOT created by /speckit.plan)
```

**Quick Start**: See [quickstart.md](./quickstart.md) for step-by-step guide to:
- Set up local PostgreSQL with Docker Compose
- Configure Exposed with built-in connection pooling
- Create first migration and table definition
- Implement first repository (EventRepositoryExposed)
- Write and run integration tests with Ktor Test Client and H2

**Schema Definitions**: See [contracts/](./contracts/) for reference SQL DDL and Exposed Table implementation guide (Phase 1-2 complete, Phase 3-4 pending)

**Implementation Note**: Schema is managed using Exposed SchemaUtils.create() - no separate migration files needed. The SQL files in contracts/ are reference documentation showing the equivalent DDL.

### Source Code (backend module only)

```text
backend/src/main/java/com/paligot/confily/backend/
├── Server.kt                        # Existing Ktor server entry point
│
├── internals/
│   └── infrastructure/
│       ├── firestore/               # Existing Firestore repositories (unchanged)
│       │   ├── ActivityEntity.kt
│       │   ├── ActivityFirestore.kt
│       │   ├── EventEntity.kt
│       │   ├── EventFirestore.kt
│       │   └── ... (all 16 entity types)
│       │
│       └── exposed/                 # NEW: Postgres repositories using Exposed
│           ├── DatabaseFactory.kt   # Connection pooling, transaction management
│           ├── tables/              # Exposed table definitions
│           │   ├── EventsTable.kt
│           │   ├── CategoriesTable.kt
│           │   ├── FormatsTable.kt
│           │   ├── SessionsTable.kt
│           │   ├── SpeakersTable.kt
│           │   ├── SessionSpeakersTable.kt  # Junction table
│           │   └── ... (all entity tables + junction tables)
│           │
│           └── repositories/        # Exposed repository implementations
│               ├── EventRepositoryExposed.kt
│               ├── EventAdminRepositoryExposed.kt
│               ├── CategoryRepositoryExposed.kt
│               ├── SessionRepositoryExposed.kt
│               └── ... (matching existing repository interfaces)
│
├── events/
│   ├── domain/
│   │   ├── EventRepository.kt       # Existing interface (unchanged)
│   │   └── EventAdminRepository.kt  # Existing interface (unchanged)
│   └── application/
│       ├── EventRepositoryDefault.kt        # Existing Firestore impl (unchanged)
│       └── EventAdminRepositoryDefault.kt   # Existing Firestore impl (unchanged)
│
├── sessions/
│   ├── domain/
│   │   ├── SessionRepository.kt     # Existing interface (unchanged)
│   │   └── SessionAdminRepository.kt
│   └── application/
│       ├── SessionRepositoryDefault.kt      # Existing Firestore impl
│       └── SessionAdminRepositoryDefault.kt
│
└── ... (categories, speakers, partners, etc. - same pattern)

backend/src/main/resources/
└── application.conf                 # Ktor config - add Postgres connection settings

backend/src/test/java/com/paligot/confily/backend/
└── internals/infrastructure/exposed/
    ├── EventRepositoryExposedTest.kt
    ├── SessionRepositoryExposedTest.kt
    └── ... (tests for all Exposed repositories)

backend/build.gradle.kts             # Add Exposed and PostgreSQL dependencies
```

**Structure Decision**: 
- **New package**: `backend/internals/infrastructure/exposed/` houses all Postgres-related code
- **Separation**: Firestore repositories remain unchanged in `firestore/` package
- **Tables vs DAOs**: Use Exposed's DSL (`Table` definitions) in `tables/` package, not DAO pattern
- **Repositories**: Each `{Entity}RepositoryExposed` implements existing domain interface from `{entity}/domain/`
- **Configuration switching**: Dependency injection (likely manual in `Server.kt` or config-based) allows choosing Firestore vs Exposed per entity
- **Schema management**: Exposed SchemaUtils.create() in DatabaseFactory for automatic table creation
- **No changes to**: Domain interfaces, application services, API routes, or client-facing code

## Complexity Tracking

> **This section documents architecture decisions and potential deviations from standard patterns**

| Decision | Why Needed | Alternative Rejected Because |
|----------|------------|------------------------------|
| Separate `exposed/` package alongside `firestore/` | Allows both implementations to coexist during development/testing | Replacing Firestore code directly would prevent rollback and comparison testing |
| Repository pattern for both Firestore and Exposed | Maintains consistent abstraction over data access | Direct DAO/Active Record pattern would violate existing domain architecture and require changing all domain interfaces |
| Normalized schemas (no JSONB) for nested data | Enforces referential integrity, enables better querying | JSONB would be easier for direct Firestore-to-Postgres mapping but loses relational benefits and query optimization |
| RESTRICT foreign key constraints | Prevents accidental data loss, forces explicit cleanup | CASCADE would risk unintended deletion of important conference data (sessions, activities) |
| Manual DI configuration for repository switching | Simple, explicit control during migration phase | Complex DI framework configuration would add overhead for temporary dual-implementation period |

**No Constitution Violations**: This implementation strictly follows backend architecture patterns, maintains modular separation, includes comprehensive testing, and meets performance requirements.
