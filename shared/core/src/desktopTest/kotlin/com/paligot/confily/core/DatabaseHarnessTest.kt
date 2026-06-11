package com.paligot.confily.core

import com.paligot.confily.core.test.inMemoryDatabase
import kotlin.test.Test
import kotlin.test.assertTrue

class DatabaseHarnessTest {
    @Test
    fun in_memory_database_enforces_foreign_keys() {
        val db = inMemoryDatabase()
        // Shape.map_id references Map(id); inserting with a missing map must fail.
        val threw = try {
            db.mapQueries.insertShape(
                order_ = 0,
                name = "x",
                description = null,
                type = "Room",
                start_x = 0.0,
                start_y = 0.0,
                end_x = 1.0,
                end_y = 1.0,
                map_id = "missing-map",
                event_id = "missing-event"
            )
            false
        } catch (e: Throwable) {
            true
        }
        assertTrue(threw, "Expected a foreign-key constraint violation")
    }
}
