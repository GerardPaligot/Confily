package com.paligot.confily.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TalkSessionInput(
    val title: String = "",
    val level: String? = null,
    val abstract: String = "",
    val category: String = "",
    val tags: List<String> = emptyList(),
    val format: String = "",
    val language: String = "",
    @SerialName("link_slides")
    val linkSlides: String? = null,
    @SerialName("link_replay")
    val linkReplay: String? = null,
    @SerialName("speaker_ids")
    val speakerIds: List<String> = emptyList()
) : Validator {
    override fun validate(): List<String> = emptyList()
}
