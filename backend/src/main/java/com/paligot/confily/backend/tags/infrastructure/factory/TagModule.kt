package com.paligot.confily.backend.tags.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.tags.application.TagAdminRepositoryDefault
import com.paligot.confily.backend.tags.application.TagAdminRepositoryExposed
import com.paligot.confily.backend.tags.application.TagRepositoryDefault
import com.paligot.confily.backend.tags.application.TagRepositoryExposed
import com.paligot.confily.backend.tags.infrastructure.firestore.TagFirestore

object TagModule {
    val tagFirestore = lazy { TagFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val tagRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            TagRepositoryExposed(PostgresModule.database)
        } else {
            TagRepositoryDefault(tagFirestore.value)
        }
    }
    val tagAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            TagAdminRepositoryExposed(PostgresModule.database)
        } else {
            TagAdminRepositoryDefault(tagFirestore.value)
        }
    }
}
