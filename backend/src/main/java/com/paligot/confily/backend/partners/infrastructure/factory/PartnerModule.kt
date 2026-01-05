package com.paligot.confily.backend.partners.infrastructure.factory

import com.paligot.confily.backend.addresses.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.partners.application.PartnerAdminRepositoryExposed
import com.paligot.confily.backend.partners.application.PartnerRepositoryExposed
import com.paligot.confily.backend.partners.infrastructure.storage.PartnerStorage

object PartnerModule {
    val partnerStorage by lazy { PartnerStorage(InternalModule.storage) }
    val partnerRepository by lazy {
        PartnerRepositoryExposed(PostgresModule.database)
    }
    val partnerAdminRepository by lazy {
        PartnerAdminRepositoryExposed(
            PostgresModule.database,
            InternalModule.commonApi,
            GeocodeModule.geocodeApi,
            partnerStorage,
            InternalModule.transcoder
        )
    }
}
