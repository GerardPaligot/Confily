package com.paligot.confily.backend.formats

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.InternalModule.database

object FormatModule {
    val formatDao = lazy { FormatDao(database.value) }
    val formatRepository = lazy { FormatRepository(eventDao.value, formatDao.value) }
}
