package com.paligot.confily.backend.qanda

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.InternalModule.database

object QAndAModule {
    val qAndADao = lazy { QAndADao(database.value) }
    val qAndARepository = lazy { QAndARepository(eventDao.value, qAndADao.value) }
}
