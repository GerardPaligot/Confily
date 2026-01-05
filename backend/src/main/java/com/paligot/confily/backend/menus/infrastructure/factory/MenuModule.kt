package com.paligot.confily.backend.menus.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.menus.application.MenuAdminRepositoryExposed

object MenuModule {
    val menuAdminRepository by lazy {
        MenuAdminRepositoryExposed(PostgresModule.database)
    }
}
