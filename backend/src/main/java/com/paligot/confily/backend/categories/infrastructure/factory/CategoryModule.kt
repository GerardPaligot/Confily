package com.paligot.confily.backend.categories.infrastructure.factory

import com.paligot.confily.backend.categories.application.CategoryAdminRepositoryDefault
import com.paligot.confily.backend.categories.application.CategoryRepositoryDefault
import com.paligot.confily.backend.categories.domain.CategoryAdminRepository
import com.paligot.confily.backend.categories.domain.CategoryRepository
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore

object CategoryModule {
    val categoryRepository = lazy<CategoryRepository> {
        CategoryRepositoryDefault(categoryFirestore.value)
    }
    val categoryAdminRepository = lazy<CategoryAdminRepository> {
        CategoryAdminRepositoryDefault(categoryFirestore.value)
    }
}
