package com.paligot.confily.backend.qanda.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.qanda.application.QAndAAdminRepositoryDefault
import com.paligot.confily.backend.qanda.application.QAndAAdminRepositoryExposed
import com.paligot.confily.backend.qanda.application.QAndARepositoryDefault
import com.paligot.confily.backend.qanda.application.QAndARepositoryExposed
import com.paligot.confily.backend.qanda.infrastructure.firestore.QAndAFirestore

object QAndAModule {
    val qAndAFirestore = lazy { QAndAFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val qAndAAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            QAndAAdminRepositoryExposed(PostgresModule.database)
        } else {
            QAndAAdminRepositoryDefault(qAndAFirestore.value)
        }
    }
    val qAndARepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            QAndARepositoryExposed(PostgresModule.database)
        } else {
            QAndARepositoryDefault(eventFirestore.value, qAndAFirestore.value)
        }
    }
}
