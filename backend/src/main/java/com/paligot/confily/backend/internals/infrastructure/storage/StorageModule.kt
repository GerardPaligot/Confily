package com.paligot.confily.backend.internals.infrastructure.storage

import com.paligot.confily.backend.events.infrastructure.storage.EventStorage
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule

object StorageModule {
    val eventStorage = lazy { EventStorage(InternalModule.storage.value) }
}
