package com.paligot.confily.backend.menus

import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.menus.application.MenuAdminRepositoryDefault
import com.paligot.confily.backend.menus.infrastructure.firestore.convertToEntity
import com.paligot.confily.models.inputs.LunchMenuInput
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class MenuAdminRepositoryDefaultTest {
    private val eventFirestore = mockk<EventFirestore>(relaxed = true)
    private val repository = MenuAdminRepositoryDefault(eventFirestore)

    @Test
    fun `update should call eventFirestore updateMenus and return eventId`() = runBlocking {
        val eventId = "event123"
        val menus = listOf(
            LunchMenuInput(
                name = "Menu 1",
                dish = "Dish 1",
                accompaniment = "Accompaniment 1",
                dessert = "Dessert 1",
                date = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toString()
            ),
            LunchMenuInput(
                name = "Menu 2",
                dish = "Dish 2",
                accompaniment = "Accompaniment 2",
                dessert = "Dessert 2",
                date = Clock.System.now().toLocalDateTime(TimeZone.UTC).date.toString()
            )
        )

        val result = repository.update(eventId, menus)

        coVerify { eventFirestore.updateMenus(eventId, eq(menus.map(LunchMenuInput::convertToEntity))) }
        assertEquals(eventId, result)
    }
}
