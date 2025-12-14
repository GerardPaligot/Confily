# Database Schema Definitions

**Feature**: 001-firestore-postgres-migration

This directory contains Exposed Table definitions and reference SQL DDL that define the PostgreSQL schema for the Confily backend.

## Table Definition Files

Tables are defined using Kotlin Exposed DSL. The SQL files here serve as reference documentation:

### Phase 1: Foundation (V1-V3)
- **V1__create_events_table.sql** - Core events table with metadata
- **V1a__create_event_features_table.sql** - Event feature flags (normalized)
- **V1b__create_event_session_tracks_table.sql** - Session tracks for schedules (normalized)
- **V1c__create_team_groups_table.sql** - Team categorization (normalized)
- **V1d__create_sponsoring_types_table.sql** - Sponsorship tiers (normalized)
- **V2__create_categories_table.sql** - Session categories
- **V3__create_formats_table.sql** - Session formatswith pronouns, email, job_title
- **V5__create_tags_table.sql** - Freeform session tags
- **V6__create_sessions_table.sql** - Conference talk sessions with link_slides, link_replay, drive_folder_id
- **V6a__create_session_categories_table.sql** - Junction table (sessions ↔ categories, many-to-many)
- **V7__create_session_speakers_table.sql** - Junction table (sessions ↔ speakers, many-to-many)
- **V8__create_session_tags_table.sql** - Junction table (sessions ↔ tags, many-to-many)
- **V8a__create_event_sessions_table.sql** - Event-type sessions (breaks, meals, networking
- **V7__create_session_speakers_table.sql** - Junction table (sessions ↔ speakers) (reference only)
- **V8__create_session_tags_table.sql** - Junction table (sessions ↔ tags) (reference only)
- **V8a__create_event_sessions_table.sql** - Event-type sessions (breaks, meals, socials) (reference only)

### Phase 3: Extended (V9-V12)
- **V9__create_partners_table.sql** - Event sponsors/partners with all media fields
- **V9a__create_partner_sponsorships_table.sql** - Multi-tier sponsorships (FK to sponsoring_types)
- **V10__create_team_table.sql** - Team members (FK to team_groups)
- **V11__create_socials_table.sql** - Social media links (event-scoped)
- **V11a__create_speaker_socials_table.sql** - Junction table (socials ↔ speakers)
- **V11b__create_partner_socials_table.sql** - Junction table (socials ↔ partners)
- **V12__create_jobs_table.sql** - Job listings (partner_id NOT NULL)

### Phase 4: Specialized (V13-V18)
- **V13__create_activities_table.sql** - Partner activities (TIMESTAMP columns)
- **V14__create_schedules_table.sql** - Time slots (references event_session_tracks, TIMESTAMP columns)
- **V15__create_maps_table.sql** - Venue floor plans
- **V15a__create_map_shapes_table.sql** - Interactive shapes on maps
- **V15b__create_map_pictograms_table.sql** - Icon markers on maps
- **V16__create_lunch_menus_table.sql** - Daily lunch menus (date + display_order)
- **V17__create_qanda_table.sql** - FAQ questions
- **V17a__create_qanda_actions_table.sql** - Action buttons for Q&A
- **V17b__create_qanda_acronyms_table.sql** - Acronym definitions for Q&A
- **V18__create_addresses_table.sql** - Physical addresses (street/postal_code NOT NULL)

## Usage

### With Exposed SchemaUtils

Define tables in `backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/`:

```kotlin
object EventsTable : UUIDTable("events") {
    val slug = varchar("slug", 100).uniqueIndex()
    val name = varchar("name", 255)
    // ... other columns
}
```

Then initialize schema in DatabaseFactory:

```kotlin
transaction {
    SchemaUtils.create(
        EventsTable,
        CategoriesTable,
        FormatsTable,
        // ... all tables
    )
}
```

```bash
# Start PostgreSQL
docker-compose up -d postgres

# Run backend (SchemaUtils creates tables automatically)
./gradlew :backend:run
```

### Schema Management

Exposed SchemaUtils provides several options:

```kotlin
// Create all tables (fails if they exist)
SchemaUtils.create(*allTables)

// Create missing tables and columns (safe for updates)
SchemaUtils.createMissingTablesAndColumns(*allTables)

// Drop and recreate (development only!)
SchemaUtils.drop(*allTables)
SchemaUtils.create(*allTables)
```31 tables total**: 16 entity types + 15 normalization/junction tables
- **59 indexes**: Including composite, full-text search (GIN), and geospatial
- **42 foreign keys**: Proper referential integrity throughout
- **5 junction tables**: session_speakers, session_categories, session_tags, speaker_socials, partner_socials
- **Normalized structure**: No nested JSONB for relational data (all normalized to 3NF)
- **Foreign key constraints**: RESTRICT for entities (explicit cleanup), CASCADE for junctions
- **Composite primary keys**: For all junction tables
- **Full-text search**: GIN indexes on sessions and speakers
- **Auto-timestamps**: created_at and updated_at with clientDefault
- *x] All 31 tables defined with SQL contracts
- [x] All foreign keys use RESTRICT (entities) or CASCADE (junctions/dependents)
- [x] All tables have created_at and updated_at timestamps
- [x] Unique constraints prevent duplicates per event
- [x] Indexes cover common query patterns (event_id, FKs, search, temporal)
- [x] Column types match data model specifications
- [x] Junction tables have composite primary keys
- [x] Normalization tables created (event_features, event_session_tracks, team_groups, sponsoring_types)
- [x] Temporal columns use TIMESTAMP (not VARCHAR)
- [x] Addresses use structured fields (street/postal_code NOT NULL

## Validation Checklist

- [ ] All foreign keys use RESTRICT (except session junction tables use CASCADE)
- [ ] All tables have created_at and updated_at timestamps with clientDefault
- [ ] Unique constraints prevent duplicates per event
- [ ] Indexes cover common query patterns (event_id, name, search)
- [ ] Column types match data model specifications
- [ ] Table definitions use Exposed DSL (UUIDTable, reference(), etc.)

## Reference

Full schema documentation: [../data-model.md](../data-model.md)  
Quick start guide: [../quickstart.md](../quickstart.md)  
Implementation plan: [../plan.md](../plan.md)
