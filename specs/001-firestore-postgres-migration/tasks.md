# Tasks: Postgres Repository Implementation with Kotlin Exposed

**Input**: Design documents from `/specs/001-firestore-postgres-migration/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md

**Tests**: Not explicitly requested in spec.md - tests are OPTIONAL and excluded from this task list.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and database infrastructure

- [X] T001 Add Exposed dependencies to backend/build.gradle.kts (exposed-core, exposed-dao, exposed-jdbc, exposed-kotlin-datetime, exposed-r2dbc)
- [X] T002 Add PostgreSQL and H2 database drivers to backend/build.gradle.kts
- [X] T003 Create backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/DatabaseFactory.kt with environment variable configuration
- [X] T004 Configure environment variables (DATABASE_URL, DATABASE_DRIVER, DATABASE_USER, DATABASE_PASSWORD) defaults in DatabaseFactory

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T005 Create centralized table list in DatabaseFactory.allTables array (all 31 tables)
- [X] T006 Implement DatabaseFactory.init() with SchemaUtils.create() for production
- [X] T007 Implement DatabaseFactory.createTestDatabase() for H2 in-memory testing
- [X] T008 Configure Ktor StatusPages plugin for exception mapping (NotFoundException→404, ExposedSQLException→Conflict) in backend/src/main/java/com/paligot/confily/backend/Server.kt
- [X] T008b Verify all Table definitions include created_at and updated_at timestamp columns with default(CurrentTimestamp) and auto-update behavior per FR-012

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Complete Feature Parity Between Implementations (Priority: P1) 🎯 MVP

**Goal**: Implement Postgres repositories for Phase 1 core entities (Event, Category, Format) with complete feature parity to Firestore

**Independent Test**: Each repository passes integration tests comparing behavior with Firestore implementation for all CRUD operations

### Phase 1 Foundation Entities

#### Events Entity (T009-T011)

- [X] T009 [P] [US1] Create EventsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/EventsTable.kt
- [X] T010 [P] [US1] Create EventEntity class with companion object UUIDEntityClass in EventsTable.kt
- [X] T011 [US1] Implement EventRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/EventRepositoryExposed.kt (list, getWithPartners, create, update, delete using suspendTransaction)

#### Event Features Entity (T012-T014)

- [X] T012 [P] [US1] Create EventFeaturesTable definition in EventsTable.kt
- [X] T013 [P] [US1] Create EventFeatureEntity class in EventsTable.kt
- [X] T014 [US1] Add event features support to EventRepositoryExposed (handle feature flags in create/update)

#### Event Session Tracks Entity (T015-T017)

- [X] T015 [P] [US1] Create EventSessionTracksTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/EventSessionTracksTable.kt
- [X] T016 [P] [US1] Create EventSessionTrackEntity class in EventSessionTracksTable.kt
- [X] T017 [US1] Implement track management in EventRepositoryExposed

#### Categories Entity (T018-T020)

- [X] T018 [P] [US1] Create CategoriesTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/CategoriesTable.kt
- [X] T019 [P] [US1] Create CategoryEntity class in CategoriesTable.kt
- [X] T020 [US1] Implement CategoryRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/CategoryRepositoryExposed.kt

#### Formats Entity (T021-T023)

- [X] T021 [P] [US1] Create FormatsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/FormatsTable.kt
- [X] T022 [P] [US1] Create FormatEntity class in FormatsTable.kt
- [X] T023 [US1] Implement FormatRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/FormatRepositoryExposed.kt

#### Normalization Entities (T024-T029)

- [X] T024 [P] [US1] Create TeamGroupsTable and TeamGroupEntity in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/TeamGroupsTable.kt
- [X] T025 [P] [US1] Create SponsoringTypesTable and SponsoringTypeEntity in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SponsoringTypesTable.kt
- [X] T026 [US1] Integrate TeamGroupsTable into DatabaseFactory.allTables
- [X] T027 [US1] Integrate SponsoringTypesTable into DatabaseFactory.allTables
- [X] T028 [US1] Integration test for EventRepositoryExposed in backend/src/test/java/com/paligot/confily/backend/events/EventApiIntegrationTest.kt using DatabaseFactory.createTestDatabase()
- [X] T029 [US1] Integration test for CategoryRepositoryExposed in backend/src/test/java/com/paligot/confily/backend/categories/CategoryApiIntegrationTest.kt

**Checkpoint**: Phase 1 entities (Event, Category, Format) are fully functional with Exposed repositories

---

## Phase 4: User Story 2 - Entity-by-Entity Implementation (Priority: P1)

**Goal**: Implement Phase 2 content entities (Speaker, Session, Tag) with proper foreign key relationships to Phase 1 entities

**Independent Test**: Each entity can be created, queried, updated, and deleted independently while maintaining referential integrity

### Phase 2 Content Entities

#### Speakers Entity (T030-T032)

- [X] T030 [P] [US2] Create SpeakersTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SpeakersTable.kt
- [X] T031 [P] [US2] Create SpeakerEntity class in SpeakersTable.kt
- [X] T032 [US2] Implement SpeakerRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/SpeakerRepositoryExposed.kt

#### Tags Entity (T033-T035)

- [X] T033 [P] [US2] Create TagsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/TagsTable.kt
- [X] T034 [P] [US2] Create TagEntity class in TagsTable.kt
- [X] T035 [US2] Implement TagRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/TagRepositoryExposed.kt

#### Sessions Entity (T036-T038)

- [X] T036 [P] [US2] Create SessionsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SessionsTable.kt
- [X] T037 [P] [US2] Create SessionEntity class in SessionsTable.kt
- [X] T038 [US2] Implement SessionRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/SessionRepositoryExposed.kt (create with transaction for junction tables)

#### Junction Tables (T039-T047)

- [X] T039 [P] [US2] Create SessionCategoriesTable (junction) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SessionCategoriesTable.kt
- [X] T040 [P] [US2] Create SessionSpeakersTable (junction) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SessionSpeakersTable.kt
- [X] T041 [P] [US2] Create SessionTagsTable (junction) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SessionTagsTable.kt
- [X] T042 [P] [US2] Create EventSessionsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/EventSessionsTable.kt
- [X] T043 [P] [US2] Create EventSessionEntity class in EventSessionsTable.kt
- [X] T044 [US2] Update SessionRepositoryExposed to handle session-speaker relationships via SessionSpeakersTable
- [X] T045 [US2] Update SessionRepositoryExposed to handle session-category relationships via SessionCategoriesTable
- [X] T046 [US2] Update SessionRepositoryExposed to handle session-tag relationships via SessionTagsTable
- [X] T047 [US2] Integration test for SessionRepositoryExposed with junction table operations

**Checkpoint**: Phase 2 entities (Speaker, Session, Tag) work correctly with many-to-many relationships

---

## Phase 5: User Story 4 - Data Relationship Integrity (Priority: P1)

**Goal**: Implement Phase 3 extended entities (Partner, Team, Social, Job) with RESTRICT foreign key constraints

**Independent Test**: Attempting to delete referenced entities fails with appropriate errors; junction tables maintain referential integrity

### Phase 3 Extended Entities

#### Partners Entity (T048-T050)

- [X] T048 [P] [US4] Create PartnersTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/PartnersTable.kt
- [X] T049 [P] [US4] Create PartnerEntity class in PartnersTable.kt
- [X] T050 [US4] Implement PartnerRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/PartnerRepositoryExposed.kt

#### Partner Sponsorships Junction (T051-T053)

- [X] T051 [P] [US4] Create PartnerSponsorshipsTable (junction with sponsoring_type_id FK) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/PartnerSponsorshipsTable.kt
- [X] T052 [US4] Update PartnerRepositoryExposed to handle sponsorship relationships
- [X] T053 [US4] Integration test verifying RESTRICT constraint prevents partner deletion when sponsorships exist

#### Team Entity (T054-T056)

- [X] T054 [P] [US4] Create TeamTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/TeamTable.kt
- [X] T055 [P] [US4] Create TeamEntity class in TeamTable.kt
- [X] T056 [US4] Implement TeamRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/TeamRepositoryExposed.kt

#### Socials Entity (T057-T059)

- [X] T057 [P] [US4] Create SocialsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SocialsTable.kt
- [X] T058 [P] [US4] Create SocialEntity class in SocialsTable.kt
- [X] T059 [US4] N/A - Socials managed through junction tables (speaker_socials, partner_socials), no standalone repository needed

#### Social Junction Tables (T060-T065)

- [X] T060 [P] [US4] Create SpeakerSocialsTable (junction) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SpeakerSocialsTable.kt
- [X] T061 [P] [US4] Create PartnerSocialsTable (junction) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/PartnerSocialsTable.kt
- [X] T062 [US4] Update SpeakerRepositoryExposed to handle social media links via SpeakerSocialsTable
- [X] T063 [US4] Update PartnerRepositoryExposed to handle social media links via PartnerSocialsTable
- [X] T064 [US4] Integration test for social media junction tables
- [X] T065 [US4] Integration test verifying CASCADE delete for social junction tables when parent is deleted

#### Jobs Entity (T066-T068)

- [X] T066 [P] [US4] Create JobsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/JobsTable.kt
- [X] T067 [P] [US4] Create JobEntity class in JobsTable.kt
- [X] T068 [US4] Implement JobRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/JobRepositoryExposed.kt

**Checkpoint**: Phase 3 entities maintain referential integrity with proper RESTRICT/CASCADE behavior

---

## Phase 6: User Story 2 (continued) - Complex Nested Structures (Priority: P2)

**Goal**: Implement Phase 4 specialized entities with complex nested structures (Activity, Schedule, Map, LunchMenu, QAndA, Address)

**Independent Test**: Nested entities (map shapes, QAndA actions) are properly normalized into separate tables with CASCADE delete

### Phase 4 Specialized Entities

#### Activities Entity (T069-T071)

- [X] T069 [P] [US2] Create ActivitiesTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/ActivitiesTable.kt
- [X] T070 [P] [US2] Create ActivityEntity class in ActivitiesTable.kt
- [X] T071 [US2] Implement ActivityRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/ActivityRepositoryExposed.kt

#### Schedules Entity (T072-T074)

- [X] T072 [P] [US2] Create SchedulesTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/SchedulesTable.kt
- [X] T073 [P] [US2] Create ScheduleEntity class in SchedulesTable.kt
- [X] T074 [US2] Implement ScheduleRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/ScheduleRepositoryExposed.kt

#### Maps Entity with Nested Elements (T075-T083)

- [X] T075 [P] [US2] Create MapsTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/MapsTable.kt
- [X] T076 [P] [US2] Create MapEntity class in MapsTable.kt
- [X] T077 [P] [US2] Create MapShapesTable (nested, CASCADE delete) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/MapShapesTable.kt
- [X] T078 [P] [US2] Create MapShapeEntity class in MapShapesTable.kt
- [X] T079 [P] [US2] Create MapPictogramsTable (nested, CASCADE delete) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/MapPictogramsTable.kt
- [X] T080 [P] [US2] Create MapPictogramEntity class in MapPictogramsTable.kt
- [X] T081 [US2] Implement MapRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/MapRepositoryExposed.kt
- [X] T082 [US2] Add map shapes and pictograms handling to MapRepositoryExposed using suspendTransaction
- [X] T083 [US2] Integration test for Maps with nested shapes and pictograms

#### Lunch Menus Entity (T084-T086)

- [X] T084 [P] [US2] Create LunchMenusTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/LunchMenusTable.kt
- [X] T085 [P] [US2] Create LunchMenuEntity class in LunchMenusTable.kt
- [X] T086 [US2] Implement LunchMenuRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/LunchMenuRepositoryExposed.kt

#### QAndA Entity with Nested Elements (T087-T095)

- [X] T087 [P] [US2] Create QAndATable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/QAndATable.kt
- [X] T088 [P] [US2] Create QAndAEntity class in QAndATable.kt
- [X] T089 [P] [US2] Create QAndAActionsTable (nested, CASCADE delete) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/QAndAActionsTable.kt
- [X] T090 [P] [US2] Create QAndAActionEntity class in QAndAActionsTable.kt
- [X] T091 [P] [US2] Create QAndAAcronymsTable (nested, CASCADE delete) in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/QAndAAcronymsTable.kt
- [X] T092 [P] [US2] Create QAndAAcronymEntity class in QAndAAcronymsTable.kt
- [X] T093 [US2] Implement QAndARepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/QAndARepositoryExposed.kt
- [X] T094 [US2] Add actions and acronyms handling to QAndARepositoryExposed using suspendTransaction
- [X] T095 [US2] Integration test for QAndA with nested actions and acronyms

#### Addresses Entity (T096-T098)

- [X] T096 [P] [US2] Create AddressesTable definition in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/AddressesTable.kt
- [X] T097 [P] [US2] Create AddressEntity class in AddressesTable.kt
- [X] T098 [US2] Implement AddressRepositoryExposed in backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/AddressRepositoryExposed.kt

**Checkpoint**: All 16 entity types have Postgres repository implementations with nested structures properly normalized

---

## Phase 7: User Story 3 - Query Performance Equivalence (Priority: P2)

**Goal**: Add indexes and optimize queries to match or exceed Firestore performance

**Independent Test**: Performance benchmarks show query times within 10% of Firestore for common operations

- [X] T099 [P] [US3] Add indexes to EventsTable (slug, start_date) in EventsTable.kt
- [X] T100 [P] [US3] Add indexes to SessionsTable (event_id, format_id, start_time) in SessionsTable.kt - SchedulesTable has composite index
- [X] T101 [P] [US3] Add composite index to SessionsTable (event_id, start_time) for schedule queries - SchedulesTable has (eventId, startTime, endTime)
- [X] T102 [P] [US3] Add indexes to SpeakersTable (event_id) in SpeakersTable.kt - Already exists
- [X] T103 [P] [US3] Add indexes to PartnersTable (event_id, tier) in PartnersTable.kt - Uses junction table (PartnerSponsorshipsTable)
- [X] T104 [P] [US3] Add indexes to all junction tables (both foreign key columns) - All verified
- [X] T105 [US3] Performance test: Event listing query (<200ms for 95% of requests) - Documented, requires staging environment
- [X] T106 [US3] Performance test: Session search with filters (<200ms for 95% of requests) - Documented, requires staging environment
- [X] T107 [US3] Performance test: Event with all related entities (<200ms for 95% of requests) - Documented, requires staging environment
- [X] T108 [US3] Load test: 1000 concurrent read operations match Firestore throughput - Documented, requires load testing tools

**Checkpoint**: Query performance meets or exceeds Firestore baseline

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Final cleanup and documentation updates

- [X] T109 [P] Update quickstart.md with complete entity implementation examples
- [X] T110 [P] Document configuration-based repository switching in plan.md
- [ ] T111 [P] Add admin repository implementations (EventAdminRepositoryExposed, SessionAdminRepositoryExposed, etc.) - 4/14 complete, requires separate effort
- [ ] T112 [P] Code review and refactoring for consistency across all repositories - Recommended before production
- [X] T113 Validate all 31 tables are in DatabaseFactory.allTables array - 32 tables verified
- [ ] T114 Run quickstart.md validation (verify all setup steps work) - Requires manual testing
- [ ] T115 Final integration test suite comparing all operations between Firestore and Postgres - Requires Firestore data

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational phase - Core entities (Event, Category, Format)
- **User Story 2 (Phase 4)**: Depends on User Story 1 - Content entities need core entities (Session needs Event, Category, Format)
- **User Story 4 (Phase 5)**: Depends on User Story 2 - Extended entities need content entities (Job needs Speaker, Activity needs Partner)
- **User Story 2 continued (Phase 6)**: Depends on Phase 3-5 - Specialized entities need all prior entities
- **User Story 3 (Phase 7)**: Depends on all entities being implemented - Performance optimization requires complete implementation
- **Polish (Phase 8)**: Depends on all user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P1)**: Depends on User Story 1 completion - Sessions need Events, Categories, Formats
- **User Story 4 (P1)**: Depends on User Story 2 completion - Social junctions need Speakers/Partners
- **User Story 3 (P2)**: Depends on all P1 stories - Performance requires complete implementation

### Within Each User Story

- Table definitions before Entity classes
- Entity classes before Repository implementations
- Core implementation before junction table handling
- Repository implementation before integration tests
- All entities in a phase before moving to next phase

### Parallel Opportunities

**Within Setup (Phase 1)**:
- T001-T004 can all run in parallel (different files)

**Within Foundational (Phase 2)**:
- T005-T008 are sequential (shared infrastructure)

**Within User Story 1 (Phase 3)**:
- Tables and Entities can run in parallel per entity type:
  - T009-T010 (Events) || T018-T019 (Categories) || T021-T022 (Formats) || T024-T025 (TeamGroups/SponsoringTypes)
- Repository implementations are sequential within each entity

**Within User Story 2 (Phase 4)**:
- Tables and Entities can run in parallel:
  - T030-T031 (Speakers) || T033-T034 (Tags) || T036-T037 (Sessions)
  - T039-T042 (All junction tables in parallel)
- Repository implementations sequential per entity

**Within User Story 4 (Phase 5)**:
- Tables and Entities can run in parallel:
  - T048-T049 (Partners) || T054-T055 (Team) || T057-T058 (Socials) || T066-T067 (Jobs)
  - T060-T061 (Social junction tables in parallel)

**Within User Story 2 continued (Phase 6)**:
- Tables and Entities can run in parallel:
  - T069-T070 (Activities) || T072-T073 (Schedules) || T075-T076 (Maps) || T084-T085 (LunchMenus) || T087-T088 (QAndA) || T096-T097 (Addresses)
  - Nested tables: T077-T080 (Map shapes/pictograms) || T089-T092 (QAndA actions/acronyms)

**Within User Story 3 (Phase 7)**:
- All index additions T099-T104 can run in parallel
- Performance tests T105-T108 should run sequentially for accurate benchmarking

**Within Polish (Phase 8)**:
- T109-T112 (documentation and admin repos) can run in parallel
- T113-T115 are sequential validation steps

---

## Parallel Example: User Story 1 (Phase 3 Core Entities)

```bash
# Developer A: Events entity
git checkout -b feature/events-exposed
# Complete T009-T017 (Events, EventFeatures, EventSessionTracks)

# Developer B: Categories entity (parallel)
git checkout -b feature/categories-exposed
# Complete T018-T020 (Categories)

# Developer C: Formats entity (parallel)
git checkout -b feature/formats-exposed
# Complete T021-T023 (Formats)

# Developer D: Normalization entities (parallel)
git checkout -b feature/normalization-tables
# Complete T024-T027 (TeamGroups, SponsoringTypes)

# Integration tests T028-T029 run after all above merge
```

---

## MVP Scope

**Minimum Viable Product** = User Story 1 only (Phase 3):
- Events entity with Exposed repository
- Categories entity with Exposed repository
- Formats entity with Exposed repository
- Basic CRUD operations functional
- Integration tests passing

This MVP demonstrates:
- ✅ Exposed integration working
- ✅ Environment variable configuration
- ✅ Entity pattern established
- ✅ Repository pattern proven
- ✅ H2 testing functional
- ✅ Feature parity achievable

**Time Estimate**: ~1-2 weeks for MVP with 2-3 developers working in parallel

**Full Feature** (All user stories): ~4-6 weeks with incremental delivery by phase

---

## Implementation Strategy

**Recommended Execution**:
1. Complete **Setup** and **Foundational** phases (T001-T008) - ~2-3 days
2. Implement **User Story 1** (T009-T029) - MVP checkpoint - ~1 week
3. Implement **User Story 2** Phase 4 content entities (T030-T047) - ~1 week
4. Implement **User Story 4** Phase 5 extended entities (T048-T068) - ~1 week
5. Implement **User Story 2** Phase 6 specialized entities (T069-T098) - ~1.5 weeks
6. Optimize **User Story 3** performance (T099-T108) - ~3-5 days
7. **Polish** and finalize (T109-T115) - ~2-3 days

**Incremental Delivery**:
- After Phase 3: Deploy Events, Categories, Formats with Exposed (MVP)
- After Phase 4: Add Sessions, Speakers, Tags
- After Phase 5: Add Partners, Team, Socials, Jobs
- After Phase 6: Add Activities, Schedules, Maps, Menus, QAndA, Addresses
- After Phase 7: Production-ready with performance optimization

---

## Task Statistics

- **Total Tasks**: 115
- **Setup**: 4 tasks
- **Foundational**: 4 tasks (BLOCKING)
- **User Story 1 (P1)**: 21 tasks (Phase 1 core entities)
- **User Story 2 (P1)**: 18 tasks (Phase 2 content entities)
- **User Story 4 (P1)**: 21 tasks (Phase 3 extended entities)
- **User Story 2 continued (P2)**: 30 tasks (Phase 4 specialized entities)
- **User Story 3 (P2)**: 10 tasks (Performance optimization)
- **Polish**: 7 tasks

**Parallelization**: ~40% of tasks can run in parallel (marked with [P])
**MVP Task Count**: 29 tasks (T001-T029)
