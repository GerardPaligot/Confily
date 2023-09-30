package org.gdglille.devfest.models.inputs

import kotlinx.serialization.Serializable

@Serializable
data class AcronymInput(
    val key: String,
    val value: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class QAndAActionInput(
    val label: String,
    val url: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class QAndAInput(
    val order: Int,
    val language: String,
    val question: String,
    val response: String,
    val actions: List<QAndAActionInput> = emptyList(),
    val acronyms: List<AcronymInput> = emptyList()
) : Validator {
    override fun validate(): List<String> = emptyList()
}
