package com.paligot.confily.backend.categories.domain

import com.paligot.confily.models.inputs.CategoryInput

interface CategoryAdminRepository {
    suspend fun create(eventId: String, category: CategoryInput): String
    suspend fun update(eventId: String, categoryId: String, input: CategoryInput): String
}
