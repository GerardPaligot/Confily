package com.paligot.confily.models.inputs

import kotlinx.serialization.Serializable

@Serializable
data class TagInput(val name: String) : Validator {
    override fun validate(): List<String> = emptyList()
}
