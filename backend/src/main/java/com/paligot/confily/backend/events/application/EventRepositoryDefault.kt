package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.domain.EventRepository
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.infrastructure.provider.convertToEntity
import com.paligot.confily.models.CreatedEvent
import com.paligot.confily.models.Event
import com.paligot.confily.models.EventList
import com.paligot.confily.models.EventV2
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.EventV4
import com.paligot.confily.models.inputs.CreatingEventInput
import java.time.LocalDateTime

class EventRepositoryDefault(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage,
    private val geocodeApi: GeocodeApi
) : EventRepository {
    override suspend fun list(): EventList {
        val events = eventFirestore.list().map { it.convertToEventItemList() }
        val now = LocalDateTime.now()
        return EventList(
            future = events
                .filter { LocalDateTime.parse(it.endDate.dropLast(1)).isAfter(now) }
                .sortedBy { it.startDate },
            past = events
                .filter { LocalDateTime.parse(it.startDate.dropLast(1)).isBefore(now) }
                .sortedBy { it.endDate }
        )
    }

    override suspend fun getWithPartners(eventId: String): Event {
        val eventDb = eventFirestore.get(eventId)
        val event = eventStorage.getEventFile(eventId, eventDb.eventUpdatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
        val partners = eventStorage.getPartnersFile(eventId, eventDb.partnersUpdatedAt)
            ?: throw NotFoundException("Partners $eventId Not Found")
        return event.convertToModel(eventEntity = eventDb, partners = partners)
    }

    override suspend fun getV2(eventId: String): EventV2 {
        val eventDb = eventFirestore.get(eventId)
        val event = eventStorage.getEventFile(eventId, eventDb.eventUpdatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
        return event.convertToModelV2(eventDb)
    }

    override suspend fun getV3(eventId: String): EventV3 {
        val eventDb = eventFirestore.get(eventId)
        val event = eventStorage.getEventFile(eventId, eventDb.eventUpdatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
        return event.convertToModelV3()
    }

    override suspend fun getV4(eventId: String): EventV4 {
        val eventDb = eventFirestore.get(eventId)
        val event = eventStorage.getEventFile(eventId, eventDb.eventUpdatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
        return event.convertToModelV4()
    }

    override suspend fun create(eventInput: CreatingEventInput, language: String): CreatedEvent {
        val addressDb = geocodeApi.geocode(eventInput.address).convertToEntity()
            ?: throw NotAcceptableException("Your address information isn't found")
        val event = eventInput.convertToEntity(addressDb, language)
        eventFirestore.createOrUpdate(event)
        return CreatedEvent(eventId = event.slugId, apiKey = event.apiKey)
    }
}
