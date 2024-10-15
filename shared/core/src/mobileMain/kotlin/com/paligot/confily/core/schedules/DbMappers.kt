package com.paligot.confily.core.schedules

import com.paligot.confily.core.extensions.formatHoursMinutes
import com.paligot.confily.db.SelectBreakSessions
import com.paligot.confily.db.SelectCategories
import com.paligot.confily.db.SelectEventSessionById
import com.paligot.confily.db.SelectFormats
import com.paligot.confily.db.SelectOpenfeedbackProjectId
import com.paligot.confily.db.SelectSessionByTalkId
import com.paligot.confily.db.SelectSessions
import com.paligot.confily.db.SelectSpeakersByTalkId
import com.paligot.confily.models.ui.AddressUi
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

private const val MaxSpeakersCount = 3

fun SelectSessions.convertTalkItemUi(
    speakers: List<SelectSpeakersByTalkId>,
    strings: Strings
): TalkItemUi {
    val startDateTime = start_time.toLocalDateTime()
    val endDateTime = end_time.toLocalDateTime()
    val diff = endDateTime.toInstant(TimeZone.UTC).minus(startDateTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    val count = (speakers.size - MaxSpeakersCount).coerceAtLeast(minimumValue = 0)
    val maxSpeakers = speakers.take(MaxSpeakersCount)
    val speakersJoined = maxSpeakers.joinToString(", ") { it.display_name }
    val level = when (level) {
        "advanced" -> strings.texts.levelAdvanced
        "intermediate" -> strings.texts.levelIntermediate
        "beginner" -> strings.texts.levelBeginner
        else -> level
    }
    return TalkItemUi(
        id = id,
        order = order_.toInt(),
        title = title,
        abstract = abstract_,
        room = room,
        level = level,
        slotTime = startDateTime.formatHoursMinutes(),
        startTime = start_time,
        endTime = end_time,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        category = convertCategoryUi(),
        speakers = maxSpeakers.map { it.display_name }.toImmutableList(),
        speakersAvatar = maxSpeakers.map { it.photo_url }.toImmutableList(),
        speakersLabel = strings.texts.speakersList(count, speakersJoined),
        isFavorite = is_favorite
    )
}

fun SelectSessionByTalkId.convertTalkUi(
    speakers: List<SelectSpeakersByTalkId>,
    openfeedbackProjectId: SelectOpenfeedbackProjectId,
    strings: Strings
): TalkUi {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val startTime = start_time.toLocalDateTime()
    val endTime = end_time.toLocalDateTime()
    val diff = endTime.toInstant(TimeZone.UTC).minus(startTime.toInstant(TimeZone.UTC))
    return TalkUi(
        title = title,
        level = level,
        abstract = abstract_,
        category = convertCategoryUi(),
        startTime = startTime.formatHoursMinutes(),
        endTime = endTime.formatHoursMinutes(),
        timeInMinutes = diff.inWholeMinutes.toInt(),
        room = room,
        speakers = speakers.map { it.convertSpeakerItemUi(strings) }.toImmutableList(),
        speakersSharing = speakers.joinToString(", ") { speaker ->
            speaker.twitter?.let { twitterName ->
                val username = twitterName.split("twitter.com/")[1]
                "${speaker.display_name} (@$username)"
            } ?: run { speaker.display_name }
        },
        canGiveFeedback = now > startTime,
        openFeedbackProjectId = openfeedbackProjectId.openfeedback_project_id,
        openFeedbackSessionId = id,
        openFeedbackUrl = open_feedback_url
    )
}

fun SelectSpeakersByTalkId.convertSpeakerItemUi(strings: Strings) = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity(strings) ?: "",
    url = photo_url
)

fun SelectSpeakersByTalkId.displayActivity(strings: Strings) = when {
    job_title != null && company != null -> strings.texts.speakerActivity(job_title!!, company!!)
    job_title == null && company != null -> company
    job_title != null && company == null -> job_title
    else -> null
}

fun SelectSessions.convertCategoryUi() = CategoryUi(
    id = category_id,
    name = categoryName,
    color = categoryColor,
    icon = categoryIcon
)

fun SelectSessionByTalkId.convertCategoryUi() = CategoryUi(
    id = category_id,
    name = categoryName,
    color = categoryColor,
    icon = categoryIcon
)

fun SelectCategories.convertCategoryUi() = CategoryUi(
    id = id,
    name = name,
    color = color,
    icon = icon
)

fun SelectFormats.convertFormatUi() = FormatUi(
    id = id,
    name = name,
    time = time.toInt()
)

fun SelectEventSessionById.convertEventSessionItemUi(strings: Strings): EventSessionItemUi {
    val startDateTime = start_time.toLocalDateTime()
    val endDateTime = end_time.toLocalDateTime()
    val diff = endDateTime.toInstant(TimeZone.UTC).minus(startDateTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    val address = if (formatted_address != null && address != null && latitude != null && longitude != null) {
        AddressUi(formatted_address!!.toImmutableList(), address!!, latitude!!, longitude!!)
    } else {
        null
    }
    return EventSessionItemUi(
        id = id,
        title = title,
        description = description,
        order = 0,
        room = room,
        slotTime = startDateTime.formatHoursMinutes(),
        startTime = start_time,
        endTime = end_time,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        addressUi = address
    )
}

fun SelectBreakSessions.convertEventSessionItemUi(strings: Strings): EventSessionItemUi {
    val startDateTime = start_time.toLocalDateTime()
    val endDateTime = end_time.toLocalDateTime()
    val diff = endDateTime.toInstant(TimeZone.UTC).minus(startDateTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    val address =
        if (formatted_address != null && address != null && latitude != null && longitude != null) {
            AddressUi(formatted_address!!.toImmutableList(), address!!, latitude!!, longitude!!)
        } else {
            null
        }
    return EventSessionItemUi(
        id = id,
        title = title,
        description = description,
        order = 0,
        room = room,
        slotTime = startDateTime.formatHoursMinutes(),
        startTime = start_time,
        endTime = end_time,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        addressUi = address
    )
}
