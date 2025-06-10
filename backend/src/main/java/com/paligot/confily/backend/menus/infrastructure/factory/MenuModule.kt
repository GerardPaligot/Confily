package com.paligot.confily.backend.menus.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.menus.application.MenuAdminRepositoryDefault

object MenuModule {
    val menuAdminRepository = lazy {
        MenuAdminRepositoryDefault(
            FirestoreModule.eventFirestore.value
        )
    }
}
