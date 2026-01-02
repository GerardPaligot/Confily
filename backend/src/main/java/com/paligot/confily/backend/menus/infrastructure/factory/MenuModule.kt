package com.paligot.confily.backend.menus.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.menus.application.MenuAdminRepositoryDefault
import com.paligot.confily.backend.menus.application.MenuAdminRepositoryExposed

object MenuModule {
    val menuAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            MenuAdminRepositoryExposed(PostgresModule.database)
        } else {
            MenuAdminRepositoryDefault(
                FirestoreModule.eventFirestore.value
            )
        }
    }
}
