package com.paligot.confily.backend.categories

import com.paligot.confily.backend.categories.application.CategoryAdminRepositoryDefault
import com.paligot.confily.backend.categories.domain.CategoryAdminRepository
import com.paligot.confily.backend.categories.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.categories.infrastructure.firestore.convertToDb
import com.paligot.confily.models.inputs.CategoryInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CategoryAdminRepositoryTest {
    private lateinit var categoryDao: CategoryFirestore
    private lateinit var repository: CategoryAdminRepository

    @BeforeTest
    fun setUp() {
        categoryDao = mockk(relaxed = true)
        repository = CategoryAdminRepositoryDefault(categoryDao)
    }

    @Test
    fun `create should call dao and return eventId`() = runBlocking {
        val eventId = "event1"
        val input = CategoryInput(name = "New Category", color = "blue", icon = "icon.png")
        val expected = input.convertToDb()
        coEvery { categoryDao.createOrUpdate(eventId, eq(expected)) } returns Unit

        val result = repository.create(eventId, input)

        assertEquals(eventId, result)
        coVerify { categoryDao.createOrUpdate(eventId, eq(expected)) }
    }

    @Test
    fun `update should call dao and return eventId`() = runBlocking {
        val eventId = "event1"
        val categoryId = "cat1"
        val input = CategoryInput(name = "Updated Category", color = "red", icon = "icon2.png")
        val expected = input.convertToDb(categoryId)
        coEvery { categoryDao.createOrUpdate(eventId, eq(expected)) } returns Unit

        val result = repository.update(eventId, categoryId, input)

        assertEquals(eventId, result)
        coVerify { categoryDao.createOrUpdate(eventId, eq(expected)) }
    }
}
