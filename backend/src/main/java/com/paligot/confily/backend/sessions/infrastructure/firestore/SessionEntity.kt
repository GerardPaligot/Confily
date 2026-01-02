package com.paligot.confily.backend.sessions.infrastructure.firestore

import com.paligot.confily.backend.addresses.infrastructure.firestore.AddressEntity

sealed class SessionEntity(
    open val id: String = "",
    open val title: String = ""
)

data class TalkSessionEntity(
    override val id: String = "",
    override val title: String = "",
    val level: String? = null,
    val abstract: String = "",
    val category: String = "",
    val format: String = "",
    val tags: List<String> = emptyList(),
    val language: String = "",
    val speakerIds: List<String> = emptyList(),
    val linkSlides: String? = null,
    val linkReplay: String? = null,
    val driveFolderId: String? = null
) : SessionEntity(id, title)

data class EventSessionEntity(
    override val id: String = "",
    override val title: String = "",
    val description: String? = null,
    val address: AddressEntity? = null
) : SessionEntity(id, title)
