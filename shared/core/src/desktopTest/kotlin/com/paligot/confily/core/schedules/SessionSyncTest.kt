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
}
