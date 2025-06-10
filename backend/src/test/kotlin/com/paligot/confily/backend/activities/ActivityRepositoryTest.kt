package com.paligot.confily.backend.activities

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.activities.application.ActivityRepositoryDefault
import com.paligot.confily.backend.internals.infrastructure.firestore.ActivityFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.models.inputs.ActivityInput
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ActivityRepositoryTest {
    private val eventDao = mockk<EventFirestore>()
    private val partnerFirestore = mockk<PartnerFirestore>()
    private val activityFirestore = mockk<ActivityFirestore>(relaxed = true)
    private val repository = ActivityRepositoryDefault(eventDao, partnerFirestore, activityFirestore)

    private val eventId = "event123"
    private val partnerId = "partner456"
    private val eventEntity = EventEntity(
        slugId = eventId,
        startDate = "2024-06-01T10:00:00Z",
        endDate = "2024-06-01T18:00:00Z"
        // other fields omitted for brevity
    )
    private val validActivity = ActivityInput(
        name = "Activity",
        startTime = "2024-06-01T12:00:00",
        endTime = "2024-06-01T13:00:00",
        partnerId = partnerId
    )

    @BeforeTest
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `create should succeed with valid input`() = runBlocking {
        every { eventDao.get(eventId) } returns eventEntity
        every { partnerFirestore.exists(eventId, partnerId) } returns true
        every { activityFirestore.createOrUpdate(any(), any()) } just Runs

        val result = repository.create(eventId, validActivity)

        assertEquals(eventId, result)
        verify { activityFirestore.createOrUpdate(eventId, any()) }
    }

    @Test
    fun `create should throw NotFoundException if partner does not exist`() = runBlocking {
        every { eventDao.get(eventId) } returns eventEntity
        every { partnerFirestore.exists(eventId, partnerId) } returns false

        val exception = assertFailsWith<NotFoundException> {
            repository.create(eventId, validActivity)
        }
        assertTrue(exception.message!!.contains(partnerId))
    }

    @Test
    fun `create should throw IllegalArgumentException if activity starts before event`(): Unit = runBlocking {
        every { eventDao.get(eventId) } returns eventEntity
        every { partnerFirestore.exists(eventId, partnerId) } returns true

        val invalidActivity = validActivity.copy(startTime = "2024-05-31T09:00:00")

        assertFailsWith<IllegalArgumentException> {
            repository.create(eventId, invalidActivity)
        }
    }

    @Test
    fun `create should throw IllegalArgumentException if activity ends after event`(): Unit = runBlocking {
        every { eventDao.get(eventId) } returns eventEntity
        every { partnerFirestore.exists(eventId, partnerId) } returns true

        val invalidActivity = validActivity.copy(endTime = "2024-06-01T19:00:00")

        assertFailsWith<IllegalArgumentException> {
            repository.create(eventId, invalidActivity)
        }
    }
}
