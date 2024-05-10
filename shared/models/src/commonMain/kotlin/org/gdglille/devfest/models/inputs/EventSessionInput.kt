package org.gdglille.devfest.models.inputs

import kotlinx.serialization.Serializable

@Serializable
data class EventSessionInput(
    val title: String? = null,
    val description: String? = null,
    val address: String? = null
) : Validator {
    override fun validate(): List<String> = emptyList()
}
