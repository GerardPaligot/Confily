package com.paligot.confily.backend.internals.infrastructure.firestore

data class AcronymEntity(
    val key: String = "",
    val value: String = ""
)

data class QAndAActionEntity(
    val order: Int = 0,
    val label: String = "",
    val url: String = ""
)

data class QAndAEntity(
    val id: String? = null,
    val order: Int = 0,
    val language: String = "",
    val question: String = "",
    val response: String = "",
    val actions: List<QAndAActionEntity> = emptyList(),
    val acronyms: List<AcronymEntity> = emptyList()
)
