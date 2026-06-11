package com.paligot.confily.core.maps

import com.paligot.confily.core.test.Fixtures
import com.paligot.confily.core.test.inMemoryDatabase
import com.paligot.confily.core.test.seedEvent
import com.paligot.confily.db.ConfilyDatabase
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertEquals

class MapSyncTest {
    private val eventId = "event-1"

    private fun dao(db: ConfilyDatabase) =
        MapDaoSQLDelight(db, Dispatchers.Unconfined)

    @Test
    fun shapes_and_pictograms_do_not_accumulate_across_syncs() {
        val db = inMemoryDatabase()
        db.seedEvent(eventId)
        val dao = dao(db)
        val maps = listOf(
            Fixtures.map(
                id = "m1",
                shapes = listOf(Fixtures.shape(0), Fixtures.shape(1)),
                pictograms = listOf(Fixtures.pictogram(0))
            )
        )

        dao.insertMaps(eventId, maps)
        dao.insertMaps(eventId, maps)

        assertEquals(2, db.mapQueries.selectShapes(eventId).executeAsList().size)
        assertEquals(1, db.mapQueries.selectPictograms(eventId).executeAsList().size)
    }

    @Test
    fun removed_map_and_its_children_are_deleted() {
        val db = inMemoryDatabase()
        db.seedEvent(eventId)
        val dao = dao(db)

        dao.insertMaps(
            eventId,
            listOf(
                Fixtures.map("m1", shapes = listOf(Fixtures.shape(0))),
                Fixtures.map("m2", shapes = listOf(Fixtures.shape(0)))
            )
        )
        dao.insertMaps(eventId, listOf(Fixtures.map("m1", shapes = listOf(Fixtures.shape(0)))))

        assertEquals(1, db.mapQueries.selectMaps(eventId).executeAsList().size)
        assertEquals(1, db.mapQueries.selectShapes(eventId).executeAsList().size)
    }
}
