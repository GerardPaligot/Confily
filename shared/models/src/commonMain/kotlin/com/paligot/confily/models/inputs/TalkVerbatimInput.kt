package com.paligot.confily.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TalkVerbatimInput(
    @SerialName("drive_name")
    val driveName: String,
    @SerialName("event_folder")
    val eventFolder: String,
    @SerialName("target_folder")
    val targetFolder: String,
    @SerialName("template_name")
    val templateName: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}
