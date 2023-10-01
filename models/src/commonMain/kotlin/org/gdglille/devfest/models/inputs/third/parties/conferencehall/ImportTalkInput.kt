package org.gdglille.devfest.models.inputs.third.parties.conferencehall

import kotlinx.serialization.Serializable
import org.gdglille.devfest.models.inputs.Validator

@Serializable
data class ImportTalkInput(
    val categories: List<CategoryInput>,
    val formats: List<FormatInput>
) : Validator {
    override fun validate(): List<String> = mutableListOf<String>().apply {
        if (categories.isEmpty()) add("Categories can't be empty")
        if (formats.isEmpty()) add("Formats can't be empty")
    }
}
