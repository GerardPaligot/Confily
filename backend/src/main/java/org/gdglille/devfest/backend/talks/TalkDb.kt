package org.gdglille.devfest.backend.talks

data class TalkDb(
    val id: String = "",
    val title: String = "",
    val level: String? = null,
    val abstract: String = "",
    val category: String = "",
    val format: String = "",
    val language: String = "",
    val speakerIds: List<String> = emptyList(),
    val linkSlides: String? = null,
    val linkReplay: String? = null
)
