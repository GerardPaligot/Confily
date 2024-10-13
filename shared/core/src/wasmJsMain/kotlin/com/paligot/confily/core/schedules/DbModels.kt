package com.paligot.confily.core.schedules

import kotlinx.serialization.Serializable

@Serializable
data class SessionDb(
    val id: String,
    val order: Long,
    val eventId: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val isFavorite: Boolean,
    val sessionEventId: String?,
    val sessionTalkId: String?
)

@Serializable
class TalkSessionDb(
    val id: String,
    val title: String,
    val abstract: String,
    val level: String?,
    val language: String?,
    val slideUrl: String?,
    val replayUrl: String?,
    val openFeedbackUrl: String?,
    val categoryId: String,
    val formatId: String,
    val eventId: String
)

@Serializable
class EventSessionDb(
    val id: String,
    val title: String,
    val description: String?,
    val formattedAddress: List<String>?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val eventId: String
)

@Serializable
class TalkSessionWithSpeakers(
    val id: Long,
    val speakerId: String,
    val talkId: String,
    val eventId: String
)

@Serializable
class SelectSessionsDb(
    val session: SessionDb,
    val talk: TalkSessionDb,
    val category: CategoryDb,
    val format: FormatDb
)

@Serializable
class SelectEventSessionsDb(
    val session: SessionDb,
    val event: EventSessionDb
)

@Serializable
class SelectTalksBySpeakerIdDb(
    val session: TalkSessionDb,
    val category: CategoryDb,
    val format: FormatDb
)

@Serializable
class CategoryDb(
    val id: String,
    val name: String,
    val color: String,
    val icon: String,
    val selected: Boolean,
    val eventId: String
)

@Serializable
class FormatDb(
    val id: String,
    val name: String,
    val time: Long,
    val selected: Boolean,
    val eventId: String
)
