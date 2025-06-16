package com.paligot.confily.backend.formats

import com.paligot.confily.backend.formats.application.FormatRepositoryDefault
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatFirestore
import com.paligot.confily.models.Format
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatRepositoryTest {
    private val formatFirestore = mockk<FormatFirestore>()
    private val repository = FormatRepositoryDefault(formatFirestore)

    @Test
    fun `list returns mapped formats from firestore`() = runBlocking {
        val eventId = "event1"
        val entities = listOf(
            FormatEntity(id = "1", name = "Talk", time = 30),
            FormatEntity(id = "2", name = "Workshop", time = 60)
        )
        coEvery { formatFirestore.getAll(eventId) } returns entities

        val result = repository.list(eventId)

        val expected = entities.map { Format(it.id!!, it.name, it.time) }
        assertEquals(expected, result)
    }

    @Test
    fun `list returns empty list when firestore returns empty`() = runBlocking {
        val eventId = "event2"
        coEvery { formatFirestore.getAll(eventId) } returns emptyList()

        val result = repository.list(eventId)

        assertEquals(emptyList(), result)
    }
}
