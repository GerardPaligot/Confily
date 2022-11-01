package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Talk(
    val id: String,
    val title: String,
    val level: String?,
    val abstract: String,
    val category: String,
    @SerialName("category_style")
    val categoryStyle: Category? = null,
    val format: String,
    val language: String,
    val speakers: List<Speaker>,
    @SerialName("link_slides")
    val linkSlides: String?,
    @SerialName("link_replay")
    val linkReplay: String?,
    @SerialName("open_feedback")
    val openFeedback: String?
)
