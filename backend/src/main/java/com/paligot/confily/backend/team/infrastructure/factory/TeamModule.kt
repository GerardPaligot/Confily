package com.paligot.confily.backend.team.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.team.infrastructure.storage.TeamStorage

object TeamModule {
    val teamStorage by lazy {
        TeamStorage(InternalModule.storage)
    }
}
