package com.paligot.confily.backend.tags.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.tags.application.TagAdminRepositoryExposed
import com.paligot.confily.backend.tags.application.TagRepositoryExposed

object TagModule {
    val tagRepository by lazy {
        TagRepositoryExposed(PostgresModule.database)
    }
    val tagAdminRepository by lazy {
        TagAdminRepositoryExposed(PostgresModule.database)
    }
}
