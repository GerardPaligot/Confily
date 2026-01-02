package com.paligot.confily.backend.team.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.team.application.TeamAdminRepositoryDefault
import com.paligot.confily.backend.team.application.TeamAdminRepositoryExposed
import com.paligot.confily.backend.team.application.TeamRepositoryDefault
import com.paligot.confily.backend.team.application.TeamRepositoryExposed
import com.paligot.confily.backend.team.infrastructure.firestore.TeamFirestore
import com.paligot.confily.backend.team.infrastructure.storage.TeamStorage

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
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            TeamRepositoryExposed(PostgresModule.database)
        } else {
            TeamRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                teamFirestore.value
            )
        }
    }
    val teamAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            TeamAdminRepositoryExposed(
                PostgresModule.database,
                InternalModule.commonApi.value,
                teamStorage.value
            )
        } else {
            TeamAdminRepositoryDefault(
                InternalModule.commonApi.value,
                FirestoreModule.eventFirestore.value,
                teamFirestore.value,
                teamStorage.value
            )
        }
    }
}
