package com.paligot.confily.backend.third.parties.openplanner.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule.partnerStorage
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule
import com.paligot.confily.backend.team.infrastructure.factory.TeamModule
import com.paligot.confily.backend.third.parties.openplanner.application.OpenPlannerRepositoryExposed
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.OpenPlannerApi

object OpenPlannerModule {
    val openPlannerApi by lazy { OpenPlannerApi.Factory.create(enableNetworkLogs = true) }
    val openPlannerRepository by lazy {
        OpenPlannerRepositoryExposed(
            PostgresModule.database,
            openPlannerApi,
            InternalModule.commonApi,
            SpeakerModule.speakerStorage,
            TeamModule.teamStorage,
            partnerStorage,
            InternalModule.transcoder
        )
    }
}
