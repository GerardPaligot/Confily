# Feature Specification: Database Migration for Improved Performance and Scalability

**Feature Branch**: `001-firestore-postgres-migration`  
**Created**: December 13, 2025  
**Status**: Draft  
**Input**: User description: "I want to refactor the backend module to migrate the firestore database to a postgres database which will use Kotlin Exposed library. In backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/firestore, I have all entities and firestore class to interact with these entities. I want to refactor entity by entity to migrate them and then, create new repository implementation in `application` packages `{Name}RepositoryExposed` classes to interact with Exposed entities and no more with Firestore ones."

**Note**: This specification describes the business value and user impact of the database migration. Technical implementation details will be addressed in the planning phase.

## Clarifications

### Session 2025-12-13

- Q: How should the system handle the migration from Firestore to Postgres (dual-write, live migration, or fresh deployment)? → A: Fresh Postgres deployment in separate cloud infrastructure, no live data migration needed. Only requirement is feature parity between Firestore and Postgres implementations.
- Q: For nested data structures like LunchMenu (with nested menu items) and Map (with location markers), how should these be represented in the Postgres schema? → A: Normalize into separate tables with foreign keys
- Q: For delete operations with cascading effects (e.g., deleting a Partner that has associated Activities), what should be the default cascade behavior? → A: RESTRICT - prevent deletion, return error if dependencies exist
- Q: For concurrent writes to the same entity in Postgres (e.g., two users updating the same session simultaneously), how should conflicts be handled? → A: Last-write-wins - no conflict detection
- Q: For many-to-many relationships (e.g., Sessions can have multiple Speakers, and Speakers can present at multiple Sessions), how should these be managed in the Postgres schema? → A: Junction table with composite primary key (session_id, speaker_id)

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Complete Feature Parity Between Implementations (Priority: P1)

API consumers need both Firestore and Postgres implementations to support identical operations (create, read, update, delete, query) for all entity types without any functional differences.

**Why this priority**: Feature parity ensures that switching between implementations doesn't break any existing functionality. This is the foundation for a successful reimplementation.

**Independent Test**: Can be fully tested by running the same test suite against both Firestore and Postgres implementations and verifying identical behavior for all CRUD operations and queries.

**Acceptance Scenarios**:

1. **Given** a create operation for any entity type, **When** executed on either implementation, **Then** the entity is persisted with all fields and relationships intact
2. **Given** a query with filters and sorting, **When** executed on either implementation, **Then** both return identical result sets in the same order
3. **Given** an update operation modifying entity relationships, **When** executed on either implementation, **Then** all related entities are updated correctly
4. **Given** a delete operation with cascading effects, **When** executed on either implementation, **Then** all dependent records are handled identically

---

### User Story 2 - Entity-by-Entity Implementation (Priority: P1)

Backend developers need to implement Postgres repositories incrementally, one entity type at a time, to validate each implementation thoroughly before proceeding to the next.

**Why this priority**: Incremental implementation reduces risk and allows for thorough testing at each step. Each entity can be validated independently before building dependencies.

**Independent Test**: Can be tested by implementing one entity type completely (schema, repository, tests) and verifying it passes all acceptance criteria before starting the next entity.

**Acceptance Scenarios**:

1. **Given** Event entity Postgres implementation is complete, **When** all CRUD operations are tested, **Then** they behave identically to the Firestore implementation
2. **Given** Session entity depends on Event and Speaker, **When** Session is implemented, **Then** all foreign key relationships work correctly
3. **Given** all core entities (Event, Category, Format) are implemented, **When** complex queries spanning these entities execute, **Then** results match Firestore implementation
4. **Given** a Postgres repository is implemented for an entity, **When** integrated with existing application code, **Then** no API contract changes are required

---

### User Story 3 - Query Performance Equivalence (Priority: P2)

API consumers need query performance on the Postgres implementation to match or exceed the current Firestore implementation for all common operations.

**Why this priority**: Performance regression would negatively impact user experience. The new implementation should maintain or improve current performance levels.

**Independent Test**: Can be tested by running performance benchmarks for common queries on both implementations and comparing response times.

**Acceptance Scenarios**:

1. **Given** a session search query with multiple filters, **When** executed on Postgres, **Then** response time is within 10% of Firestore performance
2. **Given** a query retrieving an event with all related entities (sessions, speakers, partners), **When** executed on Postgres, **Then** it completes in under 200ms for 95% of requests
3. **Given** 1000 concurrent read operations, **When** executed on Postgres, **Then** throughput matches or exceeds Firestore
4. **Given** appropriate indexes are created on Postgres tables, **When** queries execute, **Then** query plans show efficient index usage

---

### User Story 4 - Data Relationship Integrity (Priority: P1)

System administrators need guarantee that all entity relationships (foreign keys, many-to-many associations) are properly enforced in the Postgres implementation to prevent data corruption.

**Why this priority**: Data integrity is critical. Unlike document databases, relational databases can enforce constraints at the database level, improving data quality.

**Independent Test**: Can be tested by attempting to create invalid relationships and verifying that constraints are enforced correctly.

**Acceptance Scenarios**:

1. **Given** a session references a non-existent speaker, **When** attempting to save, **Then** the Postgres implementation rejects the operation with a clear error
2. **Given** a partner is deleted, **When** activities reference that partner, **Then** the system either prevents deletion or cascades appropriately based on business rules
3. **Given** many-to-many relationships (e.g., sessions to speakers), **When** relationship is created or removed, **Then** junction tables with composite primary keys maintain referential integrity
4. **Given** nested data structures (e.g., LunchMenu items normalized into separate tables), **When** parent is deleted, **Then** child record deletion is prevented by RESTRICT constraints, requiring explicit cleanup

---

### Edge Cases

- What happens when an entity in Firestore has fields that don't map cleanly to the Postgres schema?
- How does the system handle very large text fields or binary data (images, SVG maps)?
- What happens if a Postgres repository implementation is incomplete when application code tries to use it?
- What happens when query patterns used in Firestore don't have direct equivalents in SQL?
- How does the system handle transaction rollback when multi-entity operations fail partway through?
- How does the system manage very large individual records (events with 1000+ sessions)?
- How does the system prevent accidental data loss when RESTRICT constraints prevent deletion of referenced entities?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST implement Postgres repository for all existing entity types (Activity, Address, Category, Event, Format, Job, LunchMenu, Map, Partner, QAndA, Schedule, Session, Social, Speaker, Tag, Team) with complete feature parity to Firestore implementations
- **FR-002**: Each Postgres repository MUST implement the same domain repository interface as the corresponding Firestore repository to ensure API contract compatibility
- **FR-003**: System MUST preserve all data relationships between entities through proper foreign key constraints and junction tables with composite primary keys for many-to-many relationships
- **FR-004**: System MUST support all existing query patterns on the Postgres implementation, including: filtering (WHERE clauses with =, !=, >, <, IN operators), sorting (ORDER BY single/multiple columns), pagination (LIMIT/OFFSET), text search (LIKE/ILIKE patterns)
- **FR-005**: System MUST enforce data integrity rules at the database level to prevent orphaned records or broken relationships, using RESTRICT constraints to prevent deletion when dependencies exist
- **FR-006**: System MUST implement repositories entity-by-entity, allowing independent testing and validation of each implementation
- **FR-007**: System MUST convert all Firestore data types to appropriate SQL column types while preserving data meaning and structure
- **FR-008**: System MUST create necessary database indexes on commonly queried fields to ensure query performance matches or exceeds Firestore
- **FR-009**: System MUST implement efficient connection pooling to handle concurrent requests without resource exhaustion
- **FR-010**: System MUST use database transactions to ensure data consistency for operations that span multiple entities
- **FR-011**: System MUST handle nested data structures (e.g., embedded objects in Firestore documents) by normalizing them into separate tables with proper foreign key relationships
- **FR-012**: System MUST maintain audit fields (created timestamp, updated timestamp) for all entities in the Postgres schema, with last-write-wins behavior for concurrent updates
- **FR-013**: System MUST provide comprehensive test coverage comparing Firestore and Postgres repository behavior for all operations
- **FR-014**: System MUST support the same error handling and validation logic in both Firestore and Postgres implementations
- **FR-015**: System MUST allow configuration-based switching between Firestore and Postgres repositories for each entity type via environment variables (e.g., EVENT_REPOSITORY_TYPE=exposed|firestore) or Koin module configuration in Server.kt

### Key Entities

- **Activity**: Represents partner activities at events, with relationships to partners and events, containing fields like name, description, time slots
- **Address**: Represents physical locations for events and venues, containing street address, city, country, coordinates
- **Category**: Represents session categories/tracks, used to organize conference sessions
- **Event**: Core entity representing conferences/events, containing metadata like name, dates, location, configuration
- **Format**: Represents session formats (talk, workshop, keynote)
- **Job**: Represents speaker job positions and companies, linked to speaker profiles
- **LunchMenu**: Represents meal options for event days, with nested menu items and dietary information
- **Map**: Represents venue floor plans and interactive maps, containing image data and location markers
- **Partner**: Represents event sponsors and partners, with sponsorship tiers, logo, description
- **QAndA**: Represents question-answer pairs for sessions, potentially nested with threaded replies
- **Schedule**: Represents time-slotted session scheduling, with room assignments and speaker allocations
- **Session**: Core entity for conference talks/workshops, linked to speakers, categories, formats
- **Social**: Represents social media links and contacts for speakers and events
- **Speaker**: Represents conference presenters, with biography, photo, social links, job information
- **Tag**: Represents tags/topics for sessions and speakers, used for filtering and discovery
- **Team**: Represents event organizing team members, with roles and responsibilities

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: All 16 entity types have Postgres repository implementations that pass 100% of the same test suite used for Firestore repositories
- **SC-002**: All existing API endpoints function identically with Postgres repositories (same response formats, same behavior)
- **SC-003**: Postgres implementation enforces data integrity through database constraints (foreign keys validate successfully, orphaned records prevented)
- **SC-004**: System handles at least 1000 concurrent requests using Postgres repositories without errors or failures
- **SC-005**: Query performance on Postgres matches or exceeds Firestore (event listing, session search, speaker lookup complete in under 200ms for 95% of requests)
- **SC-006**: All repository unit and integration tests pass with both Firestore and Postgres implementations
- **SC-007**: Zero breaking changes to API contracts (all client applications continue to function without modifications)
- **SC-008**: System supports 10,000+ records per entity type in Postgres without performance degradation or query timeouts
- **SC-009**: Entity-by-entity implementation allows each repository to be validated independently before proceeding to the next
- **SC-010**: Configuration-based repository switching works correctly, allowing seamless toggling between Firestore and Postgres per entity type

## Dependencies

### Internal Dependencies

- Existing data models and storage layer implementations in the backend module
- Domain layer repository interfaces that define data access contracts
- Application layer implementations that orchestrate business logic
- API routing and controller layers that expose functionality to clients

### External Dependencies

- Data storage solution capable of structured data with relationships
- Database connectivity and driver support
- Connection pooling library for efficient resource management
- Schema versioning and migration tooling

### Assumptions

- Postgres database will be deployed in separate cloud infrastructure (not live migration)
- Fresh Postgres deployment starts from zero (no production data migration required)
- Firestore implementation will remain available and operational during Postgres development
- Only requirement is complete feature parity between Firestore and Postgres implementations
- Both implementations can coexist and be switched via configuration
- Standard SQL data types and JSON columns can represent all Firestore document structures
- Entity relationships in Firestore can be represented as foreign keys and junction tables in Postgres
- Test data can be created in both systems for validation purposes
- Repository interface contracts will not change (both implementations use same domain interfaces)

## Out of Scope

- Live production data migration from Firestore to Postgres
- Dual-write synchronization between Firestore and Postgres
- Automated data migration scripts for production data
- Real-time data synchronization between storage systems
- Migration of authentication and user management systems
- Storage-specific security rules (both use application-layer access control)
- Client application changes or updates
- Deletion or decommissioning of Firestore infrastructure
- Advanced performance optimization beyond basic indexing
- Multi-region data replication and disaster recovery
- Automated failover between Firestore and Postgres
- Data import/export tooling (handled separately from this implementation)

## Implementation Strategy

### Phased Approach

The Postgres repository implementation will follow an entity-by-entity approach in this recommended order:

1. **Phase 1 - Foundation**: Event, Category, Format (core configuration entities with no dependencies)
2. **Phase 2 - Content**: Speaker, Session, Tag (main content entities with relationships to Phase 1)
3. **Phase 3 - Extended**: Partner, Team, Social, Job (supporting entities)
4. **Phase 4 - Specialized**: Activity, Schedule, Map, Menu, QAndA, Address (complex nested structures)

Each phase includes:
- Postgres table schema design (primary keys, foreign keys, indexes)
- Exposed entity definition (Kotlin data classes mapped to tables)
- Repository implementation (`{Entity}RepositoryExposed` class)
- Unit tests (verify CRUD operations match Firestore behavior)
- Integration tests (verify with real Postgres database)
- Performance benchmarks (compare with Firestore)
- Code review and validation
- Merge to main branch

### Implementation Approach

Each Postgres repository (`{Entity}RepositoryExposed`) will:
- Implement the same domain repository interface as the Firestore version
- Use Exposed DSL for type-safe SQL operations
- Handle database transactions explicitly for multi-entity operations
- Convert between domain models and Exposed entity objects
- Maintain identical error handling and validation logic as Firestore implementation
- Support configuration-based switching between implementations

This ensures complete API compatibility and allows independent validation of each entity implementation.
