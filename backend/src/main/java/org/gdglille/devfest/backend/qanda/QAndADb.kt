package org.gdglille.devfest.backend.qanda

data class AcronymDb(
    val key: String = "",
    val value: String = ""
)

data class QAndAActionDb(
    val order: Int = 0,
    val label: String = "",
    val url: String = ""
)

data class QAndADb(
    val id: String? = null,
    val order: Int = 0,
    val language: String = "",
    val question: String = "",
    val response: String = "",
    val actions: List<QAndAActionDb> = emptyList(),
    val acronyms: List<AcronymDb> = emptyList()
)
