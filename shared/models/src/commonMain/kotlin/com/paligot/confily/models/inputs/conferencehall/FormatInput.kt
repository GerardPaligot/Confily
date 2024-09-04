package com.paligot.confily.models.inputs.conferencehall

import com.paligot.confily.models.inputs.Validator
import kotlinx.serialization.Serializable

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
