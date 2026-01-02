package com.paligot.confily.backend.formats.infrastructure.factory

import com.paligot.confily.backend.formats.application.FormatAdminRepositoryDefault
import com.paligot.confily.backend.formats.application.FormatAdminRepositoryExposed
import com.paligot.confily.backend.formats.application.FormatRepositoryDefault
import com.paligot.confily.backend.formats.application.FormatRepositoryExposed
import com.paligot.confily.backend.formats.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object FormatModule {
    val formatFirestore = lazy { FormatFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val formatRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            FormatRepositoryExposed(PostgresModule.database)
        } else {
            FormatRepositoryDefault(formatFirestore.value)
        }
    }
    val formatAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            FormatAdminRepositoryExposed(PostgresModule.database)
        } else {
            FormatAdminRepositoryDefault(formatFirestore.value)
        }
    }
}
