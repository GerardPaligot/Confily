package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.events.domain.EventAdminRepository
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.convertToEntity
import com.paligot.confily.models.inputs.CoCInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.FeaturesActivatedInput

class EventAdminRepositoryDefault(
    private val eventFirestore: EventFirestore,
    private val geocodeApi: GeocodeApi
) : EventAdminRepository {
    override suspend fun update(eventId: String, eventInput: EventInput): String {
        val addressEntity = eventInput.address
            ?.let {
                geocodeApi.geocode(eventInput.address!!).convertToEntity()
                    ?: throw NotAcceptableException("Your address information isn't found")
            }
        val event = eventFirestore.get(eventId)
        eventFirestore.createOrUpdate(event.convertToEntity(eventInput, addressEntity))
        return eventId
    }

    override suspend fun updateCoC(eventId: String, coc: CoCInput): String {
        eventFirestore.updateCoc(eventId, coc.coc)
        return eventId
    }

    override suspend fun updateFeatures(eventId: String, features: FeaturesActivatedInput): String {
        eventFirestore.updateFeatures(eventId, features.hasNetworking)
        return eventId
    }
}
