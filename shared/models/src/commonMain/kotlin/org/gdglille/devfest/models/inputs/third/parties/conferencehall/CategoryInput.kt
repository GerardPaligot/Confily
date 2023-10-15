package org.gdglille.devfest.models.inputs.third.parties.conferencehall

import kotlinx.serialization.Serializable
import org.gdglille.devfest.models.inputs.Validator

@Serializable
data class CategoryInput(
    val id: String,
    val color: String,
    val icon: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}
