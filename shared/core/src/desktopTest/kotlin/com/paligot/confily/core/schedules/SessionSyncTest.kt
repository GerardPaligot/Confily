package com.paligot.confily.core.schedules

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.test.Fixtures
import com.paligot.confily.core.test.inMemoryDatabase
import com.paligot.confily.core.test.seedEvent
import com.paligot.confily.db.ConfilyDatabase
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class, ExperimentalSettingsApi::class)
class SessionSyncTest {
    private val eventId = "event-1"

    private fun dao(db: ConfilyDatabase) = SessionDaoSQLDelight(
        db = db,
        settings = ConferenceSettings(MapSettings()).apply { insertEventId(eventId) },
        dispatcher = Dispatchers.Unconfined
    )

    @Test
    fun favorite_survives_a_resync() {
        val db = inMemoryDatabase()
        db.seedEvent(eventId)
        val dao = dao(db)
        val agenda = Fixtures.agenda(
            categories = listOf(Fixtures.category("c1")),
            formats = listOf(Fixtures.format("f1")),
            sessions = listOf(Fixtures.talk("t1", categoryId = "c1", formatId = "f1")),
            schedules = listOf(Fixtures.schedule("s1", sessionId = "t1"))
        )

        dao.insertAgenda(eventId, agenda)
        db.sessionQueries.markAsFavorite(is_favorite = true, id = "t1", event_id = eventId)
        dao.insertAgenda(eventId, agenda)

        assertEquals(
            1L,
            db.sessionQueries.countSessionsByFavorite(event_id = eventId, is_favorite = true)
                .executeAsOne()
        )
    }

    @Test
    fun resync_with_same_payload_does_not_duplicate_sessions() {
        val db = inMemoryDatabase()
        db.seedEvent(eventId)
        val dao = dao(db)
        val agenda = Fixtures.agenda(
            categories = listOf(Fixtures.category("c1")),
            formats = listOf(Fixtures.format("f1")),
            sessions = listOf(Fixtures.talk("t1", categoryId = "c1", formatId = "f1")),
            schedules = listOf(Fixtures.schedule("s1", sessionId = "t1"))
        )

        dao.insertAgenda(eventId, agenda)
        dao.insertAgenda(eventId, agenda)

        assertEquals(1, db.sessionQueries.selectSessions(eventId).executeAsList().size)
    }

    @Test
    fun changing_a_sessions_category_replaces_the_old_link_without_fk_crash() {
        val db = inMemoryDatabase()
        db.seedEvent(eventId)
        val dao = dao(db)
        dao.insertAgenda(
            eventId,
            Fixtures.agenda(
                categories = listOf(Fixtures.category("c1")),
                formats = listOf(Fixtures.format("f1")),
                sessions = listOf(Fixtures.talk("t1", categoryId = "c1", formatId = "f1")),
                schedules = listOf(Fixtures.schedule("s1", sessionId = "t1"))
            )
        )

        // c1 is removed from the backend; the talk now uses c2.
        dao.insertAgenda(
            eventId,
            Fixtures.agenda(
                categories = listOf(Fixtures.category("c2")),
                formats = listOf(Fixtures.format("f1")),
                sessions = listOf(Fixtures.talk("t1", categoryId = "c2", formatId = "f1")),
                schedules = listOf(Fixtures.schedule("s1", sessionId = "t1"))
            )
        )

        val rows = db.sessionQueries.selectSessions(eventId).executeAsList()
        assertEquals(1, rows.size)
        assertEquals("c2", rows.single().category_id)
    }

    @Test
    fun removed_session_speaker_tag_and_schedule_are_deleted() {
        val db = inMemoryDatabase()
        db.seedEvent(eventId)
        val dao = dao(db)
        dao.insertAgenda(
            eventId,
            Fixtures.agenda(
                categories = listOf(Fixtures.category("c1")),
                formats = listOf(Fixtures.format("f1")),
                tags = listOf(Fixtures.tag("tag1"), Fixtures.tag("tag2")),
                speakers = listOf(Fixtures.speaker("sp1"), Fixtures.speaker("sp2")),
                sessions = listOf(
                    Fixtures.talk("t1", "c1", "f1", tagIds = listOf("tag1"), speakers = listOf("sp1")),
                    Fixtures.talk("t2", "c1", "f1", tagIds = listOf("tag2"), speakers = listOf("sp2"))
                ),
                schedules = listOf(
                    Fixtures.schedule("s1", "t1"),
                    Fixtures.schedule("s2", "t2")
                )
            )
        )

        // Second sync keeps only t1 / tag1 / sp1.
        dao.insertAgenda(
            eventId,
            Fixtures.agenda(
                categories = listOf(Fixtures.category("c1")),
                formats = listOf(Fixtures.format("f1")),
                tags = listOf(Fixtures.tag("tag1")),
                speakers = listOf(Fixtures.speaker("sp1")),
                sessions = listOf(
                    Fixtures.talk("t1", "c1", "f1", tagIds = listOf("tag1"), speakers = listOf("sp1"))
                ),
                schedules = listOf(Fixtures.schedule("s1", "t1"))
            )
        )

        assertEquals(1, db.sessionQueries.selectSessions(eventId).executeAsList().size)
        assertEquals(
            emptyList(),
            db.tagQueries.selectTags(eventId).executeAsList().map { it.id }.filter { it == "tag2" }
        )
        assertEquals(0, db.speakerQueries.diffSpeakers(eventId, listOf("sp1", "sp2")).executeAsList().size)
    }
}
