package com.paligot.confily.backend.team.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.firestore.TeamFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.TeamStorage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.team.application.TeamAdminRepositoryDefault
import com.paligot.confily.backend.team.application.TeamRepositoryDefault

object TeamModule {
    val teamFirestore = lazy {
        TeamFirestore(
            SystemEnv.projectName,
            GoogleServicesModule.cloudFirestore.value
        )
    }
    val teamStorage = lazy {
        TeamStorage(InternalModule.storage.value)
    }
    val teamRepository = lazy {
        TeamRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            teamFirestore.value
        )
    }
    val teamAdminRepository = lazy {
        TeamAdminRepositoryDefault(
            InternalModule.commonApi.value,
            FirestoreModule.eventFirestore.value,
            teamFirestore.value,
            teamStorage.value
        )
    }
}
