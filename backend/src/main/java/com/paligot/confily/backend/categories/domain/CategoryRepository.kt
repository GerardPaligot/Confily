package com.paligot.confily.backend.categories.domain

import com.paligot.confily.models.Category
import com.paligot.confily.models.inputs.CategoryInput

interface CategoryRepository {
    suspend fun list(eventId: String): List<Category>
}
