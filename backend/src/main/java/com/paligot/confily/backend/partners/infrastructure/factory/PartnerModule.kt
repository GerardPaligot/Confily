package com.paligot.confily.backend.partners.infrastructure.factory

import com.paligot.confily.backend.activities.infrastructure.factory.ActivityModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.PartnerStorage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.application.PartnerAdminRepositoryDefault
import com.paligot.confily.backend.partners.application.PartnerRepositoryDefault
import com.paligot.confily.backend.third.parties.geocode.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.third.parties.welovedevs.infrastructure.factory.JobModule

object PartnerModule {
    val partnerFirestore = lazy { PartnerFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val partnerStorage = lazy { PartnerStorage(InternalModule.storage.value) }
    val partnerRepository = lazy {
        PartnerRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            partnerFirestore.value,
            ActivityModule.activityFirestore.value,
            JobModule.jobFirestore.value
        )
    }
    val partnerAdminRepository = lazy {
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
