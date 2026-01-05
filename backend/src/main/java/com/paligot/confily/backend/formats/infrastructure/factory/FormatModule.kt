package com.paligot.confily.backend.formats.infrastructure.factory

import com.paligot.confily.backend.formats.application.FormatAdminRepositoryExposed
import com.paligot.confily.backend.formats.application.FormatRepositoryExposed
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule

object FormatModule {
    val formatRepository by lazy {
        FormatRepositoryExposed(PostgresModule.database)
    }
    val formatAdminRepository by lazy {
        FormatAdminRepositoryExposed(PostgresModule.database)
    }
}
