package org.gdglille.devfest.models.inputs.third.parties.conferencehall

import kotlinx.serialization.Serializable
import org.gdglille.devfest.models.inputs.Validator

@Serializable
data class FormatInput(
    val id: String,
    val time: Int
) : Validator {
    override fun validate(): List<String> = mutableListOf<String>().apply {
        if (id == "") add("id can't be null")
        if (time < 0) add("Time can't be null")
    }
}
