# Quick Start: Postgres Repository Development

**Feature**: 001-firestore-postgres-migration  
**Audience**: Backend developers implementing Exposed repositories  
**Time to Setup**: ~15 minutes

## Prerequisites

- JDK 17+ installed
- Kotlin 2.0+ configured
- IntelliJ IDEA (recommended) or VS Code with Kotlin plugin

**Note**: Docker/PostgreSQL setup will be addressed in a future contribution. For now, development and testing use H2 in-memory database with PostgreSQL compatibility mode.

---

## Step 1: Add Dependencies to backend/build.gradle.kts

```kotlin
dependencies {
    // Existing dependencies...
    
    // Exposed ORM (Updated to 0.54.0 for Kotlin 2.0+ compatibility - T109)
    implementation("org.jetbrains.exposed:exposed-core:0.54.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.54.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.54.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.54.0")
    
    // Database drivers (PostgreSQL for production, H2 for development/testing)
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.h2database:h2:2.2.224")
    
    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.7")
    testImplementation("com.h2database:h2:2.2.224")  // PostgreSQL compatibility mode
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.0")
}
```

**Sync Gradle:**
```bash
./gradlew build --refresh-dependencies
```

---

## Step 2: Configure Environment Variables

For local development, set these environment variables (or use your IDE's run configuration):

```bash
# H2 in-memory for local development (PostgreSQL compatibility mode)
export DATABASE_URL="jdbc:h2:mem:confily_dev;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE"
export DATABASE_DRIVER="org.h2.Driver"
export DATABASE_USER="sa"
export DATABASE_PASSWORD=""
```

**For production**: Override with PostgreSQL connection details:
```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/confily_prod"
export DATABASE_DRIVER="org.postgresql.Driver"
export DATABASE_USER="confily"
export DATABASE_PASSWORD="your_secure_password"
```

**Note**: Production deployment with PostgreSQL will be configured separately.

---

## Step 3: Create Database Factory

Create `backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/DatabaseFactory.kt`:

```kotlin
package com.paligot.confily.backend.internals.infrastructure.exposed

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    
    // Centralized list of all tables for schema creation
    private val allTables = arrayOf(
        EventsTable,
        EventFeaturesTable,
        EventSessionTracksTable,
        TeamGroupsTable,
        SponsoringTypesTable,
        CategoriesTable,
        FormatsTable,
        SpeakersTable,
        TagsTable,
        SessionsTable,
        SessionCategoriesTable,
        SessionSpeakersTable,
        SessionTagsTable,
        EventSessionsTable,
        PartnersTable,
        PartnerSponsorshipsTable,
        TeamTable,
        SocialsTable,
        SpeakerSocialsTable,
        PartnerSocialsTable,
        JobsTable,
        ActivitiesTable,
        SchedulesTable,
        MapsTable,
        MapShapesTable,
        MapPictogramsTable,
        LunchMenusTable,
        QAndATable,
        QAndAActionsTable,
        QAndAAcronymsTable,
        AddressesTable
    )
    
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
    
    // For tests: create database with schema
    fun createTestDatabase(): Database {
        val database = Database.connect(
            url = "jdbc:h2:mem:test_${System.currentTimeMillis()};MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )
        
        transaction(database) {
            SchemaUtils.create(*allTables)
        }
        
        return database
    }
}
```

---

## Step 4: Create Exposed Table Definitions and Entities

Create `backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/tables/EventsTable.kt`:

```kotlin
package com.paligot.confily.backend.internals.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

// Table definition for SQL schema creation
object EventsTable : UUIDTable("events") {
    val slug = varchar("slug", 100).uniqueIndex()
    val name = varchar("name", 255)
    val startDate = date("start_date")
    val endDate = date("end_date")
    val addressId = reference("address_id", AddressesTable).nullable()
    val defaultLanguage = varchar("default_language", 10).default("en")
    val contactEmail = varchar("contact_email", 255).nullable()
    val contactPhone = varchar("contact_phone", 50).nullable()
    val cocUrl = text("coc_url").nullable()
    val faqUrl = text("faq_url").nullable()
    val publishedAt = timestamp("published_at").nullable()
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}

// Entity class for easier database access in repositories
class EventEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventEntity>(EventsTable)
    
    var slug by EventsTable.slug
    var name by EventsTable.name
    var startDate by EventsTable.startDate
    var endDate by EventsTable.endDate
    var addressId by EventsTable.addressId
    var defaultLanguage by EventsTable.defaultLanguage
    var contactEmail by EventsTable.contactEmail
    var contactPhone by EventsTable.contactPhone
    var cocUrl by EventsTable.cocUrl
    var faqUrl by EventsTable.faqUrl
    var publishedAt by EventsTable.publishedAt
    var createdAt by EventsTable.createdAt
    var updatedAt by EventsTable.updatedAt
}

object EventFeaturesTable : UUIDTable("event_features") {
    val eventId = reference("event_id", EventsTable).index()
    val featureKey = varchar("feature_key", 100)
    val enabled = bool("enabled").default(false)
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
    
    init {
        uniqueIndex(eventId, featureKey)
        index(eventId, enabled)
    }
}

class EventFeatureEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventFeatureEntity>(EventFeaturesTable)
    
    var eventId by EventFeaturesTable.eventId
    var event by EventEntity referencedOn EventFeaturesTable.eventId
    var featureKey by EventFeaturesTable.featureKey
    var enabled by EventFeaturesTable.enabled
    var createdAt by EventFeaturesTable.createdAt
    var updatedAt by EventFeaturesTable.updatedAt
}

object CategoriesTable : UUIDTable("categories") {
    val eventId = reference("event_id", EventsTable)
    val name = varchar("name", 255)
    val icon = varchar("icon", 50).nullable()
    val color = varchar("color", 20).nullable()
    val displayOrder = integer("display_order").default(0)
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
    
    init {
        uniqueIndex(eventId, name)
        index(eventId, displayOrder)
    }
}

class CategoryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CategoryEntity>(CategoriesTable)
    
    var eventId by CategoriesTable.eventId
    var event by EventEntity referencedOn CategoriesTable.eventId
    var name by CategoriesTable.name
    var icon by CategoriesTable.icon
    var color by CategoriesTable.color
    var displayOrder by CategoriesTable.displayOrder
    var createdAt by CategoriesTable.createdAt
    var updatedAt by CategoriesTable.updatedAt
}

// Additional table definitions with entities...
// (EventSessionTracksTable, TeamGroupsTable, SponsoringTypesTable, FormatsTable, etc.)
```

**Note**: 
- **Tables** define the SQL schema and are used with `SchemaUtils.create()`
- **Entities** provide a DAO pattern for simpler CRUD operations in repositories
- Entities automatically handle row mapping and provide type-safe property access

---

## Step 5: Create First Repository Implementation

Create `backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/EventRepositoryExposed.kt`:

```kotlin
package com.paligot.confily.backend.internals.infrastructure.exposed.repositories

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.domain.EventRepository
import com.paligot.confily.backend.internals.infrastructure.exposed.tables.EventEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.tables.EventsTable
import com.paligot.confily.models.*
import com.paligot.confily.models.inputs.CreatingEventInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.suspendTransaction
import java.time.Instant
import java.util.UUID

class EventRepositoryExposed(private val database: Database) : EventRepository {
    
    override suspend fun list(): EventList = suspendTransaction(db = database) {
        val events = EventEntity.all()
            .map { it.toModel() }
        EventList(events = events)
    }
    
    override suspend fun getWithPartners(eventId: String): Event = suspendTransaction(db = database) {
        EventEntity.findById(UUID.fromString(eventId))
            ?.toModel()
            ?: throw NotFoundException("Event not found: $eventId")
    }
    
    override suspend fun create(eventInput: CreatingEventInput, language: String): CreatedEvent = 
        suspendTransaction(db = database) {
            val event = EventEntity.new {
                slug = eventInput.name.lowercase().replace(" ", "-")
                name = eventInput.name
                startDate = eventInput.startDate
                endDate = eventInput.endDate
                defaultLanguage = language
                createdAt = Instant.now()
                updatedAt = Instant.now()
            }
            
            CreatedEvent(id = event.id.value.toString())
        }
    
    override suspend fun update(eventId: String, eventInput: CreatingEventInput): Unit = 
        suspendTransaction(db = database) {
            val event = EventEntity.findById(UUID.fromString(eventId))
                ?: throw NotFoundException("Event not found: $eventId")
            
            event.name = eventInput.name
            event.startDate = eventInput.startDate
            event.endDate = eventInput.endDate
            event.updatedAt = Instant.now()
        }
    
    override suspend fun delete(eventId: String): Unit = 
        suspendTransaction(db = database) {
            val event = EventEntity.findById(UUID.fromString(eventId))
                ?: throw NotFoundException("Event not found: $eventId")
            event.delete()
        }
    
    // Extension function to convert Entity to domain model
    private fun EventEntity.toModel(): Event {
        return Event(
            id = this.id.value.toString(),
            slug = this.slug,
            name = this.name,
            startDate = this.startDate,
            endDate = this.endDate,
            contactEmail = this.contactEmail,
            contactPhone = this.contactPhone,
            cocUrl = this.cocUrl,
            faqUrl = this.faqUrl,
            // Map other fields...
        )
    }
}
```

**Benefits of using Entities**:
- **Simpler CRUD**: `EventEntity.new {}`, `findById()`, `delete()` instead of manual SQL
- **Type safety**: Properties are type-checked at compile time
- **Automatic mapping**: No need for manual `ResultRow` conversion

**Exception Handling**:
- Use `NotFoundException` → mapped to HTTP 404 in StatusPages plugin
- Use `NotAcceptableException` → mapped to HTTP 406
- Use `NotAuthorized` → mapped to HTTP 401
- These exceptions are already configured in `Server.kt` StatusPages plugin
- **Relationships**: `event by EventEntity referencedOn` for foreign keys

---

## Step 6: Write Your First Integration Test

Create `backend/src/test/java/com/paligot/confily/backend/events/EventApiIntegrationTest.kt`:

```kotlin
package com.paligot.confily.backend.events

import com.paligot.confily.backend.internals.infrastructure.exposed.DatabaseFactory
import com.paligot.confily.backend.internals.infrastructure.exposed.tables.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// Test DTOs with kotlinx.serialization
@Serializable
data class CreateEventRequest(
    val name: String,
    val startDate: String,  // ISO-8601 format: "2025-05-01"
    val endDate: String,
    val contactEmail: String? = null
)

@Serializable
data class CreateEventResponse(
    val id: String
)

@Serializable
data class CreateSessionRequest(
    val title: String
)

class EventApiIntegrationTest {
    
    @Test
    fun `POST event should create in database and return 201`() = testApplication {
        val testClient = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        
        application {
            // Setup database with all tables
            val database = DatabaseFactory.createTestDatabase()
            
            // Configure application routes
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
    fun `GET events should return all events`() = testApplication {
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
        
        // Create test data using data classes
        testClient.post("/api/events") { 
            contentType(ContentType.Application.Json)
            setBody(CreateEventRequest(
                name = "Event 1",
                startDate = "2025-01-01",
                endDate = "2025-01-02"
            ))
        }
        testClient.post("/api/events") { 
            contentType(ContentType.Application.Json)
            setBody(CreateEventRequest(
                name = "Event 2",
                startDate = "2025-02-01",
                endDate = "2025-02-02"
            ))
        }
        
        // GET request
        val response = testClient.get("/api/events")
        
        assertEquals(HttpStatusCode.OK, response.status)
        // Deserialize response
        val events = response.body<List<Event>>()
        assertEquals(2, events.size)
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

**Run tests:**
```bash
./gradlew :backend:test --tests EventApiIntegrationTest
```

---

## Common Commands

```bash
# Run all tests
./gradlew :backend:test

# Run specific test
./gradlew :backend:test --tests EventApiIntegrationTest

# Run tests with verbose output
./gradlew :backend:test --info

# Clean rebuild
./gradlew clean build
```

---

## Troubleshooting

**Issue**: `Table already exists` error in tests  
**Solution**: Each test should use a unique H2 database name (e.g., `test_${System.currentTimeMillis()}`). SchemaUtils.create() will fail if tables already exist in the same database instance.

**Issue**: `Column type mismatch` errors  
**Solution**: Ensure Exposed Table definition matches your domain model. Check nullable(), default values, and column types. H2's PostgreSQL mode should handle most type compatibility.

**Issue**: Tests fail with transaction errors  
**Solution**: Ensure transactions are closed properly. Use `transaction {}` for blocking code or `suspendTransaction {}` for suspend functions. Each test should create its own database instance.

**Issue**: H2 compatibility issues with PostgreSQL-specific features  
**Solution**: H2's PostgreSQL mode supports most features, but some advanced PostgreSQL features may not work. For production validation, PostgreSQL setup will be added in a future contribution.

---

## Complete Repository Implementation Examples (T109)

### Example 1: Simple Repository with Basic CRUD

```kotlin
// backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/TagRepositoryExposed.kt
class TagRepositoryExposed(private val database: Database) : TagRepository {
    
    override suspend fun list(eventId: String): List<Tag> = suspendTransaction(database) {
        TagEntity
            .find { TagsTable.eventId eq UUID.fromString(eventId) }
            .orderBy(TagsTable.name to SortOrder.ASC)
            .map { it.toTag() }
    }
    
    private fun TagEntity.toTag() = Tag(
        id = this.id.value.toString(),
        name = this.name,
        color = this.color
    )
}
```

### Example 2: Repository with Nested Data (One-to-Many)

```kotlin
// backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/MapRepositoryExposed.kt
class MapRepositoryExposed(private val database: Database) : MapRepository {
    
    override suspend fun list(eventId: String): List<Map> = suspendTransaction(database) {
        val uuid = UUID.fromString(eventId)
        
        // Fetch all maps for event
        val maps = MapEntity
            .find { MapsTable.eventId eq uuid }
            .orderBy(MapsTable.displayOrder to SortOrder.ASC)
        
        maps.map { map ->
            val mapId = map.id.value
            
            // Query nested shapes
            val shapes = MapShapeEntity
                .find { MapShapesTable.mapId eq mapId }
                .map { it.toShape() }
            
            // Query nested pictograms
            val pictograms = MapPictogramEntity
                .find { MapPictogramsTable.mapId eq mapId }
                .map { it.toPictogram() }
            
            map.toMap(shapes, pictograms)
        }
    }
}
```

### Example 3: Repository with Multiple Junction Tables

```kotlin
// backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/SessionRepositoryExposed.kt
class SessionRepositoryExposed(private val database: Database) : SessionRepository {
    
    override suspend fun getAll(eventId: String): List<Session> = suspendTransaction(database) {
        val uuid = UUID.fromString(eventId)
        
        // Find all sessions for event
        val sessions = SessionEntity
            .find { SessionsTable.eventId eq uuid }
            .orderBy(SessionsTable.title to SortOrder.ASC)
        
        sessions.map { session ->
            val sessionId = session.id.value
            
            // Query junction tables for speakers
            val speakers = (SessionSpeakersTable innerJoin SpeakersTable)
                .select(SpeakersTable.columns)
                .where { SessionSpeakersTable.sessionId eq sessionId }
                .map { SpeakerEntity.wrapRow(it).toSpeaker() }
            
            // Query junction tables for categories
            val categories = (SessionCategoriesTable innerJoin CategoriesTable)
                .select(CategoriesTable.columns)
                .where { SessionCategoriesTable.sessionId eq sessionId }
                .map { CategoryEntity.wrapRow(it).toCategory() }
            
            session.toSession(speakers, categories)
        }
    }
}
```

### Example 4: Repository with Foreign Key Navigation

```kotlin
// backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/repositories/ScheduleRepositoryExposed.kt
class ScheduleRepositoryExposed(private val database: Database) : ScheduleRepository {
    
    override suspend fun get(eventId: String): List<Schedule> = suspendTransaction(database) {
        val uuid = UUID.fromString(eventId)
        
        ScheduleEntity
            .find { SchedulesTable.eventId eq uuid }
            .orderBy(SchedulesTable.startTime to SortOrder.ASC)
            .map { schedule ->
                Schedule(
                    id = schedule.id.value.toString(),
                    time = timeFormatter.format(schedule.startTime.toJavaInstant()),
                    startTime = timeFormatter.format(schedule.startTime.toJavaInstant()),
                    endTime = timeFormatter.format(schedule.endTime.toJavaInstant()),
                    // Navigate foreign key to get track name
                    trackId = EventSessionTrackEntity[schedule.eventSessionTrackId].trackName,
                    sessionId = schedule.sessionId?.value?.toString(),
                    eventSessionId = schedule.eventSessionId?.value?.toString()
                )
            }
    }
}
```

---

## Next Steps

1. ✅ Implement all 16 entity repositories (Completed: 100%)
2. ✅ Add 32 tables to DatabaseFactory.allTables (Verified)
3. ✅ Performance optimization with indexes (Completed)
4. ✅ Integration tests for CASCADE delete behavior
5. ⏳ Admin repository implementations (4/14 complete - see T111)
6. ⏳ Performance benchmarking in staging environment

**Ready to code!** Follow the phased implementation plan from [plan.md](./plan.md).

