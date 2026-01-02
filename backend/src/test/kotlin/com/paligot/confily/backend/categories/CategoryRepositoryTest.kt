package com.paligot.confily.backend.categories

import com.paligot.confily.backend.categories.application.CategoryRepositoryDefault
import com.paligot.confily.backend.categories.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.categories.infrastructure.firestore.CategoryFirestore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class CategoryRepositoryTest {
    private lateinit var categoryDao: CategoryFirestore
    private lateinit var repository: CategoryRepositoryDefault

    @BeforeTest
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
