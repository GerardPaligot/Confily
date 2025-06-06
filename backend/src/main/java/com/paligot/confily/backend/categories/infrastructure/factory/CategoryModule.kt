package com.paligot.confily.backend.categories.infrastructure.factory

import com.paligot.confily.backend.categories.application.CategoryAdminRepositoryDefault
import com.paligot.confily.backend.categories.application.CategoryRepositoryDefault
import com.paligot.confily.backend.categories.domain.CategoryAdminRepository
import com.paligot.confily.backend.categories.domain.CategoryRepository
import com.paligot.confily.backend.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.internals.GoogleServicesModule
import com.paligot.confily.backend.internals.SystemEnv

object CategoryModule {
    val categoryDao = lazy { CategoryFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val categoryRepository = lazy<CategoryRepository> { CategoryRepositoryDefault(categoryDao.value) }
    val categoryAdminRepository = lazy<CategoryAdminRepository> { CategoryAdminRepositoryDefault(categoryDao.value) }
}
