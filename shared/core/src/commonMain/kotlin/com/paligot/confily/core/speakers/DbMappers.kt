package com.paligot.confily.core.speakers

import com.paligot.confily.core.extensions.formatHoursMinutes
import com.paligot.confily.db.SelectSessionByTalkId
import com.paligot.confily.db.SelectSpeakersByTalkId
import com.paligot.confily.db.SelectTalksBySpeakerId
import com.paligot.confily.db.Speaker
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Speaker.convertToSpeakerUi(
    talks: ImmutableList<TalkItemUi>,
    strings: Strings
): SpeakerUi = SpeakerUi(
    name = display_name,
    pronouns = pronouns,
    bio = bio,
    jobTitle = job_title,
    company = company,
    activity = displayActivity(strings),
    url = photo_url,
    twitterUrl = twitter,
    mastodonUrl = mastodon,
    githubUrl = github,
    linkedinUrl = linkedin,
    websiteUrl = website,
    talks = talks
)

fun Speaker.convertToSpeakerItemUi(strings: Strings): SpeakerItemUi = SpeakerItemUi(
    id = id,
    name = display_name,
    pronouns = pronouns,
    company = displayActivity(strings) ?: "",
    url = photo_url
)

private fun Speaker.displayActivity(strings: Strings) = when {
    job_title != null && company != null -> strings.texts.speakerActivity(job_title!!, company!!)
    job_title == null && company != null -> company
    job_title != null && company == null -> job_title
    else -> null
}

private const val MaxSpeakersCount = 3

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

fun SelectTalksBySpeakerId.convertCategoryUi() = CategoryUi(
    id = category_id,
    name = categoryName,
    color = categoryColor,
    icon = categoryIcon
)
