# Research: Postgres Repository Implementation with Kotlin Exposed

**Feature**: 001-firestore-postgres-migration  
**Date**: December 13, 2025  
**Phase**: 0 - Research & Technology Decisions

## Research Questions

This document resolves all "NEEDS CLARIFICATION" items from the technical context and provides technology selection rationale.

---

## 1. Kotlin Exposed: DSL vs DAO Pattern

**Question**: Should we use Exposed's DSL (table definitions + query builders) or DAO pattern (object-oriented entities)?

**Decision**: **Use DSL pattern**

**Rationale**:
- **Control**: DSL provides fine-grained control over SQL generation, important for performance optimization
- **Flexibility**: Easier to write complex queries with joins, aggregations, and subqueries
- **Type Safety**: Still maintains compile-time type safety like DAO but with less abstraction overhead
- **Migration**: Closer to raw SQL, making it easier to optimize and understand query execution plans
- **Repository Pattern**: Works better with repository pattern (which we're using) since we're already abstracting data access

**Alternatives Considered**:
- DAO Pattern: More object-oriented, but adds abstraction layer we don't need since repositories already provide that
- JDBC + jOOQ: More control but loses Kotlin DSL benefits and increases boilerplate

**Resources**:
- Exposed DSL Documentation: https://github.com/JetBrains/Exposed/wiki/DSL
- Performance comparison: DSL queries are generally 5-10% faster than DAO due to less object mapping overhead

---

## 2. Connection Pooling Configuration

**Question**: How to configure database connections for optimal performance?

**Decision**: **Use Exposed's built-in connection pooling with Database.connect()**

```kotlin
Database.connect(
    url = "jdbc:postgresql://localhost:5432/confily",
    driver = "org.postgresql.Driver",
    user = config.database.user,
    password = config.database.password,
    setupConnection = { connection ->
        connection.transactionIsolation = Connection.TRANSACTION_READ_COMMITTED
        connection.schema = "public"
    },
    databaseConfig = DatabaseConfig {
        defaultMaxAttempts = 3
        defaultMinRetryDelay = 100
        defaultMaxRetryDelay = 1000
        warnLongQueriesDuration = 1000  // Log queries > 1 second
    }
)
```

**Rationale**:
- **Built-in Pooling**: Exposed manages connections efficiently without external dependencies
- **Configuration**: Can be tuned via JDBC URL parameters (e.g., `?maxPoolSize=20&minPoolSize=5`)
- **Transaction Isolation**: READ_COMMITTED balances consistency and performance
- **Retry Logic**: Automatic retry for transient failures
- **Query Monitoring**: Warns about slow queries for optimization

**Connection Pool Tuning** (via JDBC URL):
```kotlin
url = "jdbc:postgresql://localhost:5432/confily" +
      "?maxPoolSize=20" +           // Max connections for moderate concurrency
      "&minPoolSize=5" +             // Keep connections warm
      "&idleTimeout=600" +           // 10 minutes idle timeout
      "&connectionTimeout=30"        // 30 seconds connection timeout
```

**Alternatives Considered**:
- **HikariCP**: Additional dependency, more configuration overhead
- **Exposed without pooling**: Would create new connection per transaction (slower)
- **Manual connection management**: Error-prone, requires custom lifecycle handling

**Resources**:
- Exposed Database Configuration: https://github.com/JetBrains/Exposed/wiki/DataDefinition-and-DDL
- PostgreSQL JDBC Parameters: https://jdbc.postgresql.org/documentation/use/

---

## 3. Schema Migration Strategy

**Question**: How to manage database schema creation and versioning?

**Decision**: **Exposed SchemaUtils.create()**

**Rationale**:
- **Simplicity**: Schema defined directly in Kotlin Table objects, no separate SQL files
- **Type Safety**: Schema changes are compile-time checked alongside table definitions
- **Single Source of Truth**: Table definitions serve both as ORM mapping and DDL source
- **No External Dependencies**: Built into Exposed, no additional migration tools needed
- **Development Focus**: Suitable for development and testing; production migrations can be handled separately if needed
- **Idempotent**: SchemaUtils.create() only creates missing tables, safe to run multiple times

**Implementation Pattern**:
```kotlin
object DatabaseFactory {
    private val allTables = arrayOf(
        EventsTable,
        EventFeaturesTable,
        CategoriesTable,
        FormatsTable,
        SpeakersTable,
        SessionsTable,
        SessionSpeakersTable,
        // ... all 31 tables
    )
    
    fun init() {
        val database = Database.connect(...)
        
        transaction(database) {
            SchemaUtils.create(*allTables)
        }
    }
}
```

**Alternatives Considered**:
- **Flyway**: Separate SQL files add maintenance overhead; schema changes require updating both Table definitions and SQL
- **Liquibase**: XML/YAML abstraction adds complexity without benefit for our use case
- **Manual SQL scripts**: No type safety, requires manual synchronization with Table objects

**Production Considerations**:
- For production deployments, SQL DDL can be generated from Table definitions if needed
- SchemaUtils.statementsRequiredToActualizeScheme() can generate migration SQL
- Current approach optimizes for development velocity and testing

**Resources**:
- Exposed SchemaUtils: https://github.com/JetBrains/Exposed/wiki/DataDefinition-and-DDL
- Exposed Documentation: https://github.com/JetBrains/Exposed/wiki

---

## 4. Indexing Strategy for Query Performance

**Question**: Which fields need indexes to match/exceed Firestore query performance?

**Decision**: **Create indexes on all foreign keys, commonly queried fields, and composite indexes for multi-column queries**

**Index Plan**:

```sql
-- Events table
CREATE INDEX idx_events_slug ON events(slug);  -- Lookup by slug
CREATE INDEX idx_events_start_date ON events(start_date);  -- Date range queries

-- Sessions table
CREATE INDEX idx_sessions_event_id ON sessions(event_id);  -- Filter by event

CREATE INDEX idx_sessions_format_id ON sessions(format_id);  -- Filter by format
CREATE INDEX idx_sessions_start_time ON sessions(start_time);  -- Schedule queries
CREATE INDEX idx_sessions_event_start ON sessions(event_id, start_time);  -- Composite for schedule view

-- Speakers table
CREATE INDEX idx_speakers_event_id ON speakers(event_id);  -- Filter by event

-- Junction tables
CREATE INDEX idx_session_speakers_session ON session_speakers(session_id);
CREATE INDEX idx_session_speakers_speaker ON session_speakers(speaker_id);

-- Partners table
CREATE INDEX idx_partners_event_id ON partners(event_id);
CREATE INDEX idx_partners_tier ON partners(tier);  -- Filter by sponsorship tier

-- Activities table
CREATE INDEX idx_activities_event_id ON activities(event_id);
CREATE INDEX idx_activities_partner_id ON activities(partner_id);
```

**Rationale**:
- **Foreign Keys**: Always index foreign keys for join performance (PostgreSQL doesn't auto-index FKs)
- **Event Scoping**: Most queries filter by event_id (multi-tenant pattern)
- **Temporal Queries**: Indexes on dates/times for schedule views
- **Composite Indexes**: Common query patterns (event + time, event + category) benefit from composite indexes
- **Avoid Over-indexing**: Only index fields used in WHERE/JOIN clauses

**Performance Target**: <200ms for 95% of queries (per spec requirement)

**Resources**:
- PostgreSQL Index Documentation: https://www.postgresql.org/docs/current/indexes.html
- Index Advisor Tools: pg_stat_statements for query analysis

---

## 5. Handling Nested Data Structures

**Question**: How to represent Firestore nested objects (e.g., LunchMenu items, Map markers) in normalized schema?

**Decision**: **Normalize into separate tables with foreign keys** (per clarification)

**Examples**:

**Maps with Shapes and Pictograms (1-to-many nested elements)**:
```sql
CREATE TABLE maps (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    color VARCHAR(7),  -- Hex color
    color_selected VARCHAR(7),  -- Hex color when selected
    filename VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,  -- Map image/SVG URL
    filled_url TEXT,  -- Filled/highlighted version URL
    display_order INT NOT NULL DEFAULT 0,
    picto_size INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE map_shapes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    map_id UUID NOT NULL REFERENCES maps(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_x DECIMAL(10, 2) NOT NULL,
    start_y DECIMAL(10, 2) NOT NULL,
    end_x DECIMAL(10, 2) NOT NULL,
    end_y DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,  -- MappingType enum value
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE map_pictograms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    map_id UUID NOT NULL REFERENCES maps(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    position_x DECIMAL(10, 2) NOT NULL,
    position_y DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,  -- PictogramType enum value
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_maps_event_id ON maps(event_id);
CREATE INDEX idx_map_shapes_map_id ON map_shapes(map_id);
CREATE INDEX idx_map_pictograms_map_id ON map_pictograms(map_id);
```

**QAndA with Actions and Acronyms (1-to-many nested elements)**:
```sql
CREATE TABLE qanda (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES events(id) ON DELETE RESTRICT,
    display_order INT NOT NULL DEFAULT 0,
    language VARCHAR(10) NOT NULL,
    question TEXT NOT NULL,
    response TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE qanda_actions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    qanda_id UUID NOT NULL REFERENCES qanda(id) ON DELETE CASCADE,
    display_order INT NOT NULL DEFAULT 0,
    label VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE qanda_acronyms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    qanda_id UUID NOT NULL REFERENCES qanda(id) ON DELETE CASCADE,
    key VARCHAR(50) NOT NULL,
    value TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_qanda_event_id ON qanda(event_id);
CREATE INDEX idx_qanda_actions_qanda_id ON qanda_actions(qanda_id);
CREATE INDEX idx_qanda_acronyms_qanda_id ON qanda_acronyms(qanda_id);
```

**Rationale**:
- **Referential Integrity**: Foreign keys enforce data consistency
- **Queryability**: Can query/filter nested items independently
- **Flexibility**: Can add fields to nested items without schema changes to parent
- **RESTRICT Cascade**: Prevents accidental deletion of menus with items (per clarification)

**Alternatives Rejected**:
- JSONB columns: Would lose query optimization, referential integrity, and type safety
- Embedded JSON in TEXT: No indexing or querying capability

---

## 6. Transaction Management with Exposed

**Question**: How to properly manage database transactions for multi-entity operations?

**Decision**: **Use Exposed's `suspendTransaction {}` blocks with explicit transaction boundaries**

**Pattern**:
```kotlin
import org.jetbrains.exposed.sql.transactions.experimental.suspendTransaction

class SessionRepositoryExposed(private val database: Database) : SessionRepository {
    
    override suspend fun create(eventId: String, session: SessionInput): Session = suspendTransaction(db = database) {
        // All DB operations within this block are in a single transaction
        
        // 1. Insert session
        val sessionId = SessionsTable.insertAndGetId {
            it[event] = UUID.fromString(eventId)
            it[title] = session.title
            it[description] = session.description
            // ...
        }
        
        // 2. Insert session-speaker relationships (junction table)
        session.speakerIds.forEach { speakerId ->
            SessionSpeakersTable.insert {
                it[sessionRef] = sessionId
                it[speaker] = UUID.fromString(speakerId)
            }
        }
        
        // 3. Return created session (query within same transaction)
        getById(sessionId.value.toString())
    }
    
    override suspend fun delete(eventId: String, sessionId: String): Unit = suspendTransaction(db = database) {
        // Transaction ensures all-or-nothing deletion
        SessionSpeakersTable.deleteWhere { sessionRef eq UUID.fromString(sessionId) }
        SessionsTable.deleteWhere { id eq UUID.fromString(sessionId) }
    }
}
```

**Rationale**:
- **Atomicity**: Multi-step operations (insert session + junction records) succeed or fail together
- **Consistency**: Enforces data integrity across related entities
- **Coroutine Support**: `suspendTransaction` from exposed-r2dbc integrates with Kotlin suspend functions
- **Isolation**: Default READ COMMITTED isolation level sufficient for conference management workload

**Error Handling**:
Error handling is managed by Ktor's StatusPages plugin, not in repository code. Database exceptions are automatically caught and mapped to HTTP status codes:

```kotlin
// In Server.kt - StatusPages plugin configuration
install(StatusPages) {
    exception<ExposedSQLException> { call, cause ->
        when {
            cause.message?.contains("foreign key") == true -> 
                call.respond(HttpStatusCode.Conflict, "Referenced entity not found")
            cause.message?.contains("unique") == true -> 
                call.respond(HttpStatusCode.Conflict, "Entity already exists")
            else -> 
                call.respond(HttpStatusCode.InternalServerError, "Database error")
        }
    }
    exception<NotFoundException> { call, cause ->
        call.respond(HttpStatusCode.NotFound, cause.message ?: "Not found")
    }
}
```

Repositories throw domain exceptions which are mapped by StatusPages:
```kotlin
override suspend fun get(id: String): Session = suspendTransaction(db = database) {
    SessionEntity.findById(UUID.fromString(id))?.toModel()
        ?: throw NotFoundException("Session not found: $id")
}
```

**Resources**:
- Exposed Transactions: https://github.com/JetBrains/Exposed/wiki/Transactions
- PostgreSQL Isolation Levels: https://www.postgresql.org/docs/current/transaction-iso.html

---

## 7. Testing Strategy with H2 In-Memory Database

**Question**: How to test Exposed repositories efficiently without Docker dependencies?

**Decision**: **Use H2 in-memory database with Ktor Test Client for integration tests**

**Test Pattern**:
```kotlin
@Serializable
data class CreateEventRequest(
    val name: String,
    val startDate: String,
    val endDate: String,
    val contactEmail: String? = null
)

@Serializable
data class CreateEventResponse(
    val id: String
)

class EventApiIntegrationTest {
    
    @Test
    fun `POST event should create in database`() = testApplication {
        val testClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        
        application {
            // Use DatabaseFactory helper from production code
            val database = DatabaseFactory.createTestDatabase()
            
            // Configure routes
            configureRouting()
            configureSerialization()
        }
        
        // Create request using type-safe data class
        val request = CreateEventRequest(
            name = "KotlinConf 2025",
            startDate = "2025-05-01",
            endDate = "2025-05-03",
            contactEmail = "info@kotlinconf.com"
        )
        
        // Make HTTP request with automatic serialization
        val response = testClient.post("/api/events") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        
        // Assert HTTP response and deserialize
        assertEquals(HttpStatusCode.Created, response.status)
        val createdEvent = response.body<CreateEventResponse>()
        assertNotNull(createdEvent.id)
        
        // Verify entity was created in database
        transaction(database) {
            val events = EventEntity.all().toList()
            assertEquals(1, events.size)
            assertEquals("KotlinConf 2025", events[0].name)
            assertEquals("info@kotlinconf.com", events[0].contactEmail)
        }
    }
    
    @Test
    fun `DELETE event with sessions should return 409 Conflict`() = testApplication {
        val testClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        
        application {
            val database = DatabaseFactory.createTestDatabase()
            configureRouting()
            configureSerialization()
        }
        
        // Create event using data class
        val createResponse = testClient.post("/api/events") { 
            contentType(ContentType.Application.Json)
            setBody(CreateEventRequest(
                name = "Test Event",
                startDate = "2025-01-01",
                endDate = "2025-01-02"
            ))
        }
        val createdEvent = createResponse.body<CreateEventResponse>()
        
        // Create session linked to event
        testClient.post("/api/events/${createdEvent.id}/sessions") {
            contentType(ContentType.Application.Json)
            setBody(CreateSessionRequest(title = "Test Session"))
        }
        
        // Try to delete event (should fail due to RESTRICT constraint)
        val deleteResponse = testClient.delete("/api/events/${createdEvent.id}")
        
        assertEquals(HttpStatusCode.Conflict, deleteResponse.status)
    }
}
```

**Rationale**:
- **Fast Execution**: In-memory database starts instantly, no Docker required
- **Isolated Tests**: Each test gets fresh database, no state pollution
- **CI/CD Friendly**: Runs on any platform without Docker daemon
- **Full Stack Testing**: Tests HTTP layer + repository + database together
- **PostgreSQL Compatibility**: H2's PostgreSQL mode supports most syntax

**Alternatives Rejected**:
- Testcontainers: Slower, requires Docker, overkill for most tests
- Repository-only tests: Doesn't test HTTP serialization, routing, error handling
- Mocking: Doesn't test actual SQL generation or database behavior

**Resources**:
- H2 Database: https://h2database.com
- Ktor Testing: https://ktor.io/docs/testing.html
- Exposed with H2: https://github.com/JetBrains/Exposed/wiki/DataDefinition-and-DDL

---

## 8. Configuration Management

**Question**: How to configure database connections (development, staging, production)?

**Decision**: **Environment variables with System.getenv()**

**Implementation in DatabaseFactory**:
```kotlin
object DatabaseFactory {
    
    fun init() {
        // Read from environment variables with H2 defaults for local dev
        val url = System.getenv("DATABASE_URL") 
            ?: "jdbc:h2:mem:confily_dev;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE"
        val driver = System.getenv("DATABASE_DRIVER") 
            ?: "org.h2.Driver"
        val user = System.getenv("DATABASE_USER") 
            ?: "sa"
        val password = System.getenv("DATABASE_PASSWORD") 
            ?: ""
        
        // Connect with Exposed's built-in connection pooling
        val database = Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password,
            databaseConfig = DatabaseConfig {
                warnLongQueriesDuration = 1000  // Log queries > 1 second
            }
        )
        
        // Create database schema
        transaction(database) {
            SchemaUtils.create(*allTables)
        }
    }
}
```

**Environment Variables (Development)**:
```bash
# H2 in-memory for local development (PostgreSQL compatibility mode)
export DATABASE_URL="jdbc:h2:mem:confily_dev;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE"
export DATABASE_DRIVER="org.h2.Driver"
export DATABASE_USER="sa"
export DATABASE_PASSWORD=""
```

**Environment Variables (Production)**:
```bash
# PostgreSQL for production
export DATABASE_URL="jdbc:postgresql://prod-db.example.com:5432/confily_prod"
export DATABASE_DRIVER="org.postgresql.Driver"
export DATABASE_USER="confily_prod"
export DATABASE_PASSWORD="<secure-password>"
```

**Rationale**:
- **Simplicity**: No configuration files, no external dependencies (no Typesafe Config)
- **Security**: Passwords in env vars, never committed to git
- **12-Factor**: Follows 12-factor app principles
- **Defaults**: Sensible defaults for local development (H2 in-memory)
- **Flexibility**: Easy to switch between H2 and PostgreSQL by changing env vars

---

## 9. Dependency Versions

**Final Dependency Selections** (add to `backend/build.gradle.kts`):

```kotlin
dependencies {
    // Existing dependencies
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    // ...
    
    // NEW: PostgreSQL + Exposed
    implementation("org.jetbrains.exposed:exposed-core:0.46.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.46.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.46.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.46.0")  // For kotlinx-datetime support
    implementation("org.jetbrains.exposed:exposed-r2dbc:0.46.0")  // For suspendTransaction()
    implementation("org.postgresql:postgresql:42.7.1")
    
    // NEW: Testing
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.7")
    testImplementation("com.h2database:h2:2.2.224")  // PostgreSQL compatibility mode
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.0")
}
```

**Version Compatibility**:
- Exposed 0.46.0: Latest stable, compatible with Kotlin 2.0
- PostgreSQL JDBC 42.7.1: Latest with PostgreSQL 14+ support
- H2 2.2.224: PostgreSQL compatibility mode for testing
- Ktor Test 2.3.7: Matches server version
- JUnit Jupiter 5.10.1: Latest JUnit 5

---

## Summary

All research questions resolved. Key decisions:
1. ✅ **Exposed DSL** pattern for type-safe SQL
2. ✅ **Exposed's built-in connection pooling** via Database.connect()
7. ✅ **H2 in-memory database** with Ktor Test Client for fast integration testing
4. ✅ **Comprehensive indexing** on FKs and query fields
5. ✅ **Normalized schemas** for nested data
6. ✅ **`transaction {}` blocks** for multi-entity operations
7. ✅ **H2 in-memory database** with Ktor Test Client for integration testing
8. ✅ **Typesafe Config** with environment variable overrides
9. ✅ **Dependency versions** selected and compatible

**Ready for Phase 1**: Data model design and schema definition.
