package com.paligot.confily.backend.categories.domain

import com.paligot.confily.models.Category

interface CategoryRepository {
    suspend fun list(eventId: String): List<Category>
}
