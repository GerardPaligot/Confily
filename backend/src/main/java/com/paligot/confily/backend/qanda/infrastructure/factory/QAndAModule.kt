package com.paligot.confily.backend.qanda.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.qanda.application.QAndAAdminRepositoryExposed
import com.paligot.confily.backend.qanda.application.QAndARepositoryExposed

object QAndAModule {
    val qAndAAdminRepository by lazy {
        QAndAAdminRepositoryExposed(PostgresModule.database)
    }
    val qAndARepository by lazy {
        QAndARepositoryExposed(PostgresModule.database)
    }
}
