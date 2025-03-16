package com.paligot.confily.core.schedules

import com.paligot.confily.core.schedules.entities.Address
import com.paligot.confily.core.schedules.entities.EventSession
import com.paligot.confily.core.schedules.entities.EventSessionItem
import com.paligot.confily.core.schedules.entities.FeedbackConfig
import com.paligot.confily.core.schedules.entities.Level
import com.paligot.confily.core.schedules.entities.SelectableCategory
import com.paligot.confily.core.schedules.entities.SelectableFormat
import com.paligot.confily.core.schedules.entities.Session
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.speakers.entities.SpeakerItem
import com.paligot.confily.db.SelectOpenfeedbackProjectId
import com.paligot.confily.db.SelectSessionByTalkId
import com.paligot.confily.db.SelectSessions
import com.paligot.confily.db.SelectSpeakersByTalkId
import com.paligot.confily.db.SelectSpeakersByTalkIds
import com.paligot.confily.db.SelectTalksBySpeakerId
import kotlinx.datetime.LocalDateTime
import com.paligot.confily.core.schedules.entities.Category as CategoryEntity
import com.paligot.confily.core.schedules.entities.Format as FormatEntity

internal fun SelectSessionByTalkId.mapToEntity(
    speakers: List<SpeakerItem>,
    openfeedbackProjectId: SelectOpenfeedbackProjectId
): Session = Session(
    id = id,
    title = title,
    abstract = abstract_,
    category = CategoryEntity(
        id = category_id,
        name = categoryName,
        color = categoryColor,
        icon = categoryIcon
    ),
    format = FormatEntity(
        id = format_id,
        name = formatName,
        time = time.toInt()
    ),
    level = when (level?.lowercase()) {
        "advanced" -> Level.Advanced
        "intermediate" -> Level.Intermediate
        "beginner" -> Level.Beginner
        else -> null
    },
    language = language,
    startTime = LocalDateTime.parse(start_time),
    endTime = LocalDateTime.parse(end_time),
    room = room,
    speakers = speakers,
    feedback = if (openfeedbackProjectId.openfeedback_project_id != null && open_feedback_url != null) {
        FeedbackConfig(
            projectId = openfeedbackProjectId.openfeedback_project_id!!,
            sessionId = id,
            url = open_feedback_url!!
        )
    } else {
        null
    }
)

internal fun SelectSessions.mapToEntity(speakers: List<SpeakerItem>): SessionItem {
    val startTime = LocalDateTime.parse(start_time)
    return SessionItem(
        id = id,
        order = order_.toInt(),
        title = title,
        category = CategoryEntity(
            id = category_id,
            name = categoryName,
            color = categoryColor,
            icon = categoryIcon
        ),
        format = FormatEntity(
            id = format_id,
            name = formatName,
            time = time.toInt()
        ),
        level = when (level?.lowercase()) {
            "advanced" -> Level.Advanced
            "intermediate" -> Level.Intermediate
            "beginner" -> Level.Beginner
            else -> null
        },
        room = room,
        language = language,
        startTime = startTime,
        endTime = LocalDateTime.parse(end_time),
        speakers = speakers,
        isFavorite = is_favorite
    )
}

internal fun SelectTalksBySpeakerId.mapToEntity(speakers: List<SpeakerItem>): SessionItem {
    val startTime = LocalDateTime.parse(start_time)
    return SessionItem(
        id = id,
        order = order_.toInt(),
        title = title,
        category = CategoryEntity(
            id = category_id,
            name = categoryName,
            color = categoryColor,
            icon = categoryIcon
        ),
        format = FormatEntity(
            id = format_id,
            name = formatName,
            time = time.toInt()
        ),
        level = when (level?.lowercase()) {
            "advanced" -> Level.Advanced
            "intermediate" -> Level.Intermediate
            "beginner" -> Level.Beginner
            else -> null
        },
        room = room,
        language = language,
        startTime = startTime,
        endTime = LocalDateTime.parse(end_time),
        speakers = speakers,
        isFavorite = is_favorite
    )
}

internal fun SelectSpeakersByTalkId.mapToEntity(): SpeakerItem = SpeakerItem(
    id = id,
    displayName = display_name,
    photoUrl = photo_url,
    jobTitle = job_title,
    company = company
)

internal fun SelectSpeakersByTalkIds.mapToEntity(): SpeakerItem = SpeakerItem(
    id = id,
    displayName = display_name,
    photoUrl = photo_url,
    jobTitle = job_title,
    company = company
)

internal val eventSessionItemMapper = { id: String, title: String, description: String?,
    order: Long, start_time: String, end_time: String, room: String ->
    EventSessionItem(
        id = id,
        order = order.toInt(),
        title = title,
        description = description,
        room = room,
        startTime = LocalDateTime.parse(start_time),
        endTime = LocalDateTime.parse(end_time)
    )
}

internal val eventSessionMapper = { id: String, title: String, description: String?,
    formatted_address: List<String>?, latitude: Double?, longitude: Double?,
    start_time: String, end_time: String, room: String ->
    val startTime = LocalDateTime.parse(start_time)
    EventSession(
        id = id,
        title = title,
        description = description ?: "",
        room = room,
        startTime = startTime,
        endTime = LocalDateTime.parse(end_time),
        address = if (formatted_address != null && latitude != null && longitude != null) {
            Address(
                formatted = formatted_address,
                latitude = latitude,
                longitude = longitude
            )
        } else {
            null
        }
    )
}

internal val categoryMapper = { id: String, name: String, color: String, icon: String,
    selected: Boolean ->
    SelectableCategory(
        id = id,
        name = name,
        color = color,
        icon = icon,
        selected = selected
    )
}

internal val formatMapper = { id: String, name: String, time: Long, selected: Boolean ->
    SelectableFormat(
        id = id,
        name = name,
        time = time.toInt(),
        selected = selected
    )
}
