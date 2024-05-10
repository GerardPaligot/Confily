package org.gdglille.devfest.backend.sessions

import org.gdglille.devfest.backend.events.AddressDb

sealed class SessionDb(
    open val id: String = "",
    open val title: String = ""
)

data class TalkDb(
    override val id: String = "",
    override val title: String = "",
    val level: String? = null,
    val abstract: String = "",
    val category: String = "",
    val format: String = "",
    val language: String = "",
    val speakerIds: List<String> = emptyList(),
    val linkSlides: String? = null,
    val linkReplay: String? = null
) : SessionDb(id, title)

data class EventSessionDb(
    override val id: String = "",
    override val title: String = "",
    val description: String? = null,
    val address: AddressDb? = null
) : SessionDb(id, title)
