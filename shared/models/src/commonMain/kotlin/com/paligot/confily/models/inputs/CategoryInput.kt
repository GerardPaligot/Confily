package com.paligot.confily.models.inputs

import kotlinx.serialization.Serializable

@Serializable
data class CategoryInput(
    val name: String,
    val color: String,
    val icon: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}
