package com.paligot.confily.backend.partners.infrastructure.factory

import com.paligot.confily.backend.activities.infrastructure.factory.ActivityModule
import com.paligot.confily.backend.addresses.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.application.PartnerAdminRepositoryDefault
import com.paligot.confily.backend.partners.application.PartnerAdminRepositoryExposed
import com.paligot.confily.backend.partners.application.PartnerRepositoryDefault
import com.paligot.confily.backend.partners.application.PartnerRepositoryExposed
import com.paligot.confily.backend.partners.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.backend.partners.infrastructure.storage.PartnerStorage

object PartnerModule {
    val partnerFirestore = lazy { PartnerFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val partnerStorage = lazy { PartnerStorage(InternalModule.storage.value) }
    val partnerRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            PartnerRepositoryExposed(PostgresModule.database)
        } else {
            PartnerRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                partnerFirestore.value,
                ActivityModule.activityFirestore.value,
                JobModule.jobFirestore.value
            )
        }
    }
    val partnerAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            PartnerAdminRepositoryExposed(
                PostgresModule.database,
                InternalModule.commonApi.value,
                GeocodeModule.geocodeApi.value,
                partnerStorage.value,
                InternalModule.transcoder.value
            )
        } else {
            PartnerAdminRepositoryDefault(
                GeocodeModule.geocodeApi.value,
                InternalModule.commonApi.value,
                FirestoreModule.eventFirestore.value,
                partnerFirestore.value,
                partnerStorage.value,
                InternalModule.transcoder.value
            )
        }
    }
}
