package org.gdglille.devfest.database.mappers

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.android.shared.resources.Strings
import org.gdglille.devfest.db.SelectBreakSessions
import org.gdglille.devfest.db.SelectCategories
import org.gdglille.devfest.db.SelectFormats
import org.gdglille.devfest.db.SelectOpenfeedbackProjectId
import org.gdglille.devfest.db.SelectSessionByTalkId
import org.gdglille.devfest.db.SelectSessions
import org.gdglille.devfest.db.SelectSpeakersByTalkId
import org.gdglille.devfest.db.SelectTalksBySpeakerId
import org.gdglille.devfest.db.Session
import org.gdglille.devfest.db.TalkSession
import org.gdglille.devfest.db.TalkSessionWithSpeakers
import org.gdglille.devfest.extensions.formatHoursMinutes
import org.gdglille.devfest.models.ScheduleItemV3
import org.gdglille.devfest.models.TalkV3
import org.gdglille.devfest.models.ui.CategoryUi
import org.gdglille.devfest.models.ui.FormatUi
import org.gdglille.devfest.models.ui.TalkItemUi
import org.gdglille.devfest.models.ui.TalkUi

private const val BREAK_TITLE = "break"
private const val MaxSpeakersCount = 3

fun SelectSessions.convertCategoryUi() = CategoryUi(
    id = category_id,
    name = categoryName,
    color = categoryColor,
    icon = categoryIcon
)

fun SelectBreakSessions.convertCategoryUi() = CategoryUi(
    id = id,
    name = BREAK_TITLE,
    color = "",
    icon = null
)

fun SelectTalksBySpeakerId.convertCategoryUi() = CategoryUi(
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

fun SelectBreakSessions.convertTalkItemUi(strings: Strings): TalkItemUi {
    val startDateTime = start_time.toLocalDateTime()
    val endDateTime = end_time.toLocalDateTime()
    val diff = endDateTime.toInstant(TimeZone.UTC).minus(startDateTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    return TalkItemUi(
        id = id,
        order = order_.toInt(),
        title = strings.titles.agendaBreak,
        abstract = "",
        room = room,
        level = null,
        slotTime = startDateTime.formatHoursMinutes(),
        startTime = start_time,
        endTime = end_time,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        category = convertCategoryUi(),
        speakers = persistentListOf(),
        speakersAvatar = persistentListOf(),
        speakersLabel = "",
        isFavorite = false
    )
}

fun SelectTalksBySpeakerId.convertTalkItemUi(
    session: SelectSessionByTalkId,
    speakers: List<SelectSpeakersByTalkId>,
    strings: Strings
): TalkItemUi {
    val startTime = session.start_time.toLocalDateTime()
    val endTime = session.end_time.toLocalDateTime()
    val diff = endTime.toInstant(TimeZone.UTC).minus(startTime.toInstant(TimeZone.UTC))
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
        order = session.order_.toInt(),
        title = title,
        abstract = abstract_,
        room = session.room,
        level = level,
        slotTime = startTime.formatHoursMinutes(),
        startTime = session.start_time,
        endTime = session.end_time,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        category = convertCategoryUi(),
        speakers = maxSpeakers.map { it.display_name }.toImmutableList(),
        speakersAvatar = maxSpeakers.map { it.photo_url }.toImmutableList(),
        speakersLabel = strings.texts.speakersList(count, speakersJoined),
        isFavorite = session.is_favorite
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
            if (speaker.twitter == null) {
                speaker.display_name
            } else {
                val twitter = speaker.twitter.split("twitter.com/").get(1)
                "${speaker.display_name} (@$twitter)"
            }
        },
        canGiveFeedback = now > startTime,
        openFeedbackProjectId = openfeedbackProjectId.openfeedback_project_id,
        openFeedbackSessionId = id,
        openFeedbackUrl = open_feedback_url
    )
}

fun ScheduleItemV3.convertToDb(eventId: String): Session = Session(
    id = this.id,
    order_ = order.toLong(),
    room = this.room,
    date = this.date,
    start_time = this.startTime,
    end_time = this.endTime,
    talk_id = talkId,
    event_id = eventId,
    is_favorite = false
)

fun TalkV3.convertToDb(eventId: String): TalkSession = TalkSession(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract_ = this.abstract,
    language = this.language,
    slide_url = this.linkSlides,
    replay_url = this.linkReplay,
    category_id = this.categoryId,
    format_id = this.formatId,
    open_feedback_url = this.openFeedback,
    event_id = eventId
)

fun TalkV3.convertToDb(eventId: String, speakerId: String) = TalkSessionWithSpeakers(
    id = 0L,
    speaker_id = speakerId,
    talk_id = id,
    event_id = eventId
)
