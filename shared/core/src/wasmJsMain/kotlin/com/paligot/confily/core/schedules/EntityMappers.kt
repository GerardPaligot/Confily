package com.paligot.confily.core.schedules

import com.paligot.confily.core.schedules.entities.Address
import com.paligot.confily.core.schedules.entities.Category
import com.paligot.confily.core.schedules.entities.EventSession
import com.paligot.confily.core.schedules.entities.EventSessionItem
import com.paligot.confily.core.schedules.entities.Format
import com.paligot.confily.core.schedules.entities.Level
import com.paligot.confily.core.schedules.entities.SelectableCategory
import com.paligot.confily.core.schedules.entities.SelectableFormat
import com.paligot.confily.core.schedules.entities.Session
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.schedules.entities.SpeakerItem
import com.paligot.confily.core.speakers.SpeakerDb
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun SelectSessionsDb.mapToSessionEntity(speakers: List<SpeakerItem>): Session = Session(
    id = talk.id,
    title = talk.title,
    abstract = talk.abstract,
    category = category.mapTopEntity(),
    format = format.mapTopEntity(),
    level = when (talk.level?.lowercase()) {
        "advanced" -> Level.Advanced
        "intermediate" -> Level.Intermediate
        "beginner" -> Level.Beginner
        else -> null
    },
    language = talk.language,
    startTime = Instant.parse(session.startTime).toLocalDateTime(TimeZone.currentSystemDefault()),
    endTime = Instant.parse(session.endTime).toLocalDateTime(TimeZone.currentSystemDefault()),
    room = session.room,
    speakers = speakers,
    feedback = null
)

fun SelectSessionsDb.mapToEntity(speakers: List<SpeakerItem>): SessionItem = SessionItem(
    id = session.id,
    title = talk.title,
    category = Category(
        id = category.id,
        name = category.name,
        color = category.color,
        icon = category.icon
    ),
    format = Format(
        id = format.id,
        name = format.name,
        time = format.time.toInt()
    ),
    level = when (talk.level?.lowercase()) {
        "advanced" -> Level.Advanced
        "intermediate" -> Level.Intermediate
        "beginner" -> Level.Beginner
        else -> null
    },
    room = session.room,
    language = talk.language,
    order = session.order.toInt(),
    startTime = Instant.parse(session.startTime)
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    endTime = Instant.parse(session.endTime).toLocalDateTime(TimeZone.currentSystemDefault()),
    speakers = speakers,
    isFavorite = session.isFavorite
)

fun SpeakerDb.mapToEntity(): SpeakerItem = SpeakerItem(
    id = id,
    displayName = displayName,
    photoUrl = photoUrl,
    jobTitle = jobTitle,
    company = company
)

fun SelectEventSessionsDb.mapToEntity(): EventSession {
    val address =
        if (event.formattedAddress != null && event.latitude != null && event.longitude != null) {
            Address(
                formatted = event.formattedAddress,
                latitude = event.latitude,
                longitude = event.longitude
            )
        } else {
            null
        }
    return EventSession(
        id = event.id,
        title = event.title,
        description = event.description ?: "",
        room = session.room,
        startTime = Instant.parse(session.startTime)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        endTime = Instant.parse(session.endTime).toLocalDateTime(TimeZone.currentSystemDefault()),
        address = address
    )
}

fun SelectEventSessionsDb.mapToItemEntity(): EventSessionItem = EventSessionItem(
    id = event.id,
    order = session.order.toInt(),
    title = event.title,
    description = event.description ?: "",
    room = session.room,
    startTime = Instant.parse(session.startTime)
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    endTime = Instant.parse(session.endTime).toLocalDateTime(TimeZone.currentSystemDefault())
)

fun CategoryDb.mapToEntity() = Category(
    id = id,
    name = name,
    color = color,
    icon = icon
)

fun CategoryDb.mapTopEntity() = SelectableCategory(
    id = id,
    name = name,
    color = color,
    icon = icon,
    selected = selected
)

fun FormatDb.mapToEntity() = Format(
    id = id,
    name = name,
    time = time.toInt()
)

fun FormatDb.mapTopEntity() = SelectableFormat(
    id = id,
    name = name,
    time = time.toInt(),
    selected = selected
)
