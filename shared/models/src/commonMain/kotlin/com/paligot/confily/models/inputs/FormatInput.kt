package com.paligot.confily.models.inputs

import kotlinx.serialization.Serializable

@Serializable
data class FormatInput(
    val name: String,
    val time: Int
) : Validator {
    override fun validate(): List<String> = mutableListOf<String>().apply {
        if (name == "") add("Name can't be empty")
        if (time < 0) add("Time can't be negative")
    }
}
