package com.paligot.confily.models.inputs.conferencehall

import com.paligot.confily.models.inputs.Validator
import kotlinx.serialization.Serializable

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
