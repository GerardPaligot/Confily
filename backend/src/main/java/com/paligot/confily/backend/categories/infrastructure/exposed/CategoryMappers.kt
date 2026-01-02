package com.paligot.confily.backend.categories.infrastructure.exposed

import com.paligot.confily.models.Category

fun CategoryEntity.toModel(): Category = Category(
    id = this.id.value.toString(),
    name = this.name,
    color = this.color ?: "#000000",
    icon = this.icon ?: ""
)
