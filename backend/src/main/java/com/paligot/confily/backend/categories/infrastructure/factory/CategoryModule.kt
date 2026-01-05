package com.paligot.confily.backend.categories.infrastructure.factory

import com.paligot.confily.backend.categories.application.CategoryAdminRepositoryExposed
import com.paligot.confily.backend.categories.application.CategoryRepositoryExposed
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule

object CategoryModule {
    val categoryRepository by lazy {
        CategoryRepositoryExposed(PostgresModule.database)
    }
    val categoryAdminRepository by lazy {
        CategoryAdminRepositoryExposed(PostgresModule.database)
    }
}
