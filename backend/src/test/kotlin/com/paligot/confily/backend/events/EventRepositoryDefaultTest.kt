package com.paligot.confily.backend.events

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.events.application.EventRepositoryDefault
import com.paligot.confily.backend.events.domain.EventRepository
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.AddressComponent
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.Geocode
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.Geometry
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.Location
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.Result
import com.paligot.confily.models.EventList
import com.paligot.confily.models.inputs.CreatingEventInput
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class EventRepositoryDefaultTest {
    @Test
    fun `list returns events from firestore`() = runBlocking {
        // Given
        val mockFirestore = mockk<EventFirestore>()
        coEvery { mockFirestore.list() } returns emptyList()
        val repository: EventRepository = EventRepositoryDefault(
            eventFirestore = mockFirestore,
            eventStorage = mockk(), // Mock other dependencies as needed
            geocodeApi = mockk() // Mock other dependencies as needed
        )

        // When
        val result = repository.list()

        // Then
        assertEquals(expected = EventList(future = emptyList(), past = emptyList()), actual = result)
    }

    @Test
    fun `list returns future and past events correctly`() = runBlocking {
        // Given
        val mockFirestore = mockk<EventFirestore>()
        val now = LocalDateTime.now()
        coEvery { mockFirestore.list() } returns listOf(
            EventEntity(
                startDate = now.plusDays(1).toString() + "Z",
                endDate = now.plusDays(2).toString() + "Z"
            ),
            EventEntity(
                startDate = now.minusDays(2).toString() + "Z",
                endDate = now.minusDays(1).toString() + "Z"
            )
        )
        val repository: EventRepository = EventRepositoryDefault(
            eventFirestore = mockFirestore,
            eventStorage = mockk(), // Mock other dependencies as needed
            geocodeApi = mockk() // Mock other dependencies as needed
        )

        // When
        val result = repository.list()

        // Then
        assertEquals(1, result.future.size)
        assertEquals(1, result.past.size)
    }

    @Test
    fun `create throws NotAcceptableException when address is not found`() {
        // Given
        val mockFirestore = mockk<EventFirestore>()
        val mockStorage = mockk<EventStorage>()
        val mockGeocodeApi = mockk<GeocodeApi>()
        val eventInput = CreatingEventInput(
            name = "Event name",
            year = "2025",
            address = "123 Main St, City, Country",
            startDate = "2025-01-01T10:00:00Z",
            endDate = "2025-01-02T10:00:00Z",
            contactEmail = "",
            contactPhone = ""
        )
        val repository = EventRepositoryDefault(mockFirestore, mockStorage, mockGeocodeApi)
        coEvery { mockGeocodeApi.geocode(any()) } returns Geocode(results = emptyList(), status = "ZERO_RESULTS")

        // When & Then
        assertFailsWith<NotAcceptableException> {
            runBlocking { repository.create(eventInput, "en") }
        }
    }

    @Test
    fun `create returns CreatedEvent when address is found`(): Unit = runBlocking {
        // Given
        val mockFirestore = mockk<EventFirestore>(relaxed = true)
        val mockStorage = mockk<EventStorage>(relaxed = true)
        val mockGeocodeApi = mockk<GeocodeApi>()
        val repository = EventRepositoryDefault(mockFirestore, mockStorage, mockGeocodeApi)
        val eventInput = CreatingEventInput(
            name = "Event name",
            year = "2025",
            address = "123 Main St, City, Country",
            startDate = "2025-01-01T10:00:00Z",
            endDate = "2025-01-02T10:00:00Z",
            contactEmail = "",
            contactPhone = ""
        )
        coEvery { mockGeocodeApi.geocode(any()) } returns Geocode(
            results = listOf(
                Result(
                    formattedAddress = "123 Main St, City, Country",
                    addressComponents = listOf(
                        AddressComponent("country", "Country", types = emptyList()),
                        AddressComponent("locality", "City", types = emptyList())
                    ),
                    geometry = Geometry(Location(12.34, 56.78)),
                    placeId = "",
                    types = emptyList()
                )
            ),
            status = "OK"
        )
        coEvery { mockFirestore.createOrUpdate(any()) } returns Unit

        // When
        val result = repository.create(eventInput, "en")

        // Then
        assertEquals("event-name", result.eventId)
        assertNotNull(result.apiKey)
    }
}
