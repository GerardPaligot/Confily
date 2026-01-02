package com.paligot.confily.backend.categories.infrastructure.factory

import com.paligot.confily.backend.categories.application.CategoryAdminRepositoryDefault
import com.paligot.confily.backend.categories.application.CategoryAdminRepositoryExposed
import com.paligot.confily.backend.categories.application.CategoryRepositoryDefault
import com.paligot.confily.backend.categories.application.CategoryRepositoryExposed
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object CategoryModule {
    val categoryRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            CategoryRepositoryExposed(PostgresModule.database)
        } else {
            CategoryRepositoryDefault(categoryFirestore.value)
        }
    }
    val categoryAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            CategoryAdminRepositoryExposed(PostgresModule.database)
        } else {
            CategoryAdminRepositoryDefault(categoryFirestore.value)
        }
    }
}
