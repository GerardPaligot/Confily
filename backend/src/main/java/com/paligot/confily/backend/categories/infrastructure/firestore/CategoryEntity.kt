package com.paligot.confily.backend.categories.infrastructure.firestore

data class CategoryEntity(
    val id: String? = null,
    val name: String = "",
    val color: String = "",
    val icon: String = ""
)
