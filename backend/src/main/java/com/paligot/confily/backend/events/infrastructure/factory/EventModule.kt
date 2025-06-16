package com.paligot.confily.backend.events.infrastructure.factory

import com.paligot.confily.backend.events.application.EventAdminRepositoryDefault
import com.paligot.confily.backend.events.application.EventRepositoryDefault
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.StorageModule
import com.paligot.confily.backend.third.parties.geocode.infrastructure.factory.GeocodeModule

object EventModule {
    val eventRepository = lazy {
        EventRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            StorageModule.eventStorage.value,
            GeocodeModule.geocodeApi.value
        )
    }
    val eventAdminRepository = lazy {
        EventAdminRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            GeocodeModule.geocodeApi.value
        )
    }
}
