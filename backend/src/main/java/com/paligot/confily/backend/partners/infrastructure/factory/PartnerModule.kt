package com.paligot.confily.backend.partners.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.partners.application.PartnerRepositoryExposed
import com.paligot.confily.backend.partners.infrastructure.storage.PartnerStorage

object PartnerModule {
    val partnerStorage by lazy { PartnerStorage(InternalModule.storage) }
    val partnerRepository by lazy {
        PartnerRepositoryExposed(PostgresModule.database)
    }
}
