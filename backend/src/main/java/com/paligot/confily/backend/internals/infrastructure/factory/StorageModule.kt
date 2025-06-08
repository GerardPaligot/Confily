package com.paligot.confily.backend.internals.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage

object StorageModule {
    val eventStorage = lazy { EventStorage(InternalModule.storage.value) }
}
