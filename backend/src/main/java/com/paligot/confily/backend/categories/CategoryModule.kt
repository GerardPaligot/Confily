package com.paligot.confily.backend.categories

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.InternalModule.database

object CategoryModule {
    val categoryDao = lazy { CategoryDao(database.value) }
    val categoryRepository = lazy { CategoryRepository(eventDao.value, categoryDao.value) }
}
