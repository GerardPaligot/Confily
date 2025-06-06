package com.paligot.confily.backend.categories

import com.paligot.confily.backend.categories.application.CategoryRepositoryDefault
import com.paligot.confily.backend.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.infrastructure.firestore.CategoryEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CategoryRepositoryTest {
    private lateinit var categoryDao: CategoryFirestore
    private lateinit var repository: CategoryRepositoryDefault

    @Before
    fun setUp() {
        categoryDao = mockk(relaxed = true)
        repository = CategoryRepositoryDefault(categoryDao)
    }

    @Test
    fun `list should return categories from dao`() = runBlocking {
        val eventId = "event1"
        coEvery { categoryDao.getAll(eventId) } returns listOf(CategoryEntity(id = "id", name = "name"))

        val result = repository.list(eventId)

        assertEquals(1, result.size)
        assertEquals("id", result[0].id)
        coVerify { categoryDao.getAll(eventId) }
    }
}
