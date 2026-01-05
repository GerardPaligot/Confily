package com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.factory

import com.paligot.confily.backend.addresses.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.third.parties.partnersconnect.application.PartnersConnectRepositoryExposed

object PartnersConnectModule {
    val partnersConnectRepository by lazy {
        PartnersConnectRepositoryExposed(PostgresModule.database, GeocodeModule.geocodeApi)
    }
}
