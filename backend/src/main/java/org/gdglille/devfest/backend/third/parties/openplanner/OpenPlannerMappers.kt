package org.gdglille.devfest.backend.third.parties.openplanner

import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.formats.FormatDb
import org.gdglille.devfest.backend.schedulers.ScheduleDb
import org.gdglille.devfest.backend.speakers.SpeakerDb
import org.gdglille.devfest.backend.talks.TalkDb

fun CategoryOP.convertToDb() = CategoryDb(
    id = id,
    name = name,
    color = color,
    icon = ""
)

fun FormatOP.convertToDb() = FormatDb(
    id = id,
    name = name,
    time = durationMinutes
)

fun SpeakerOP.convertToDb(): SpeakerDb {
    val twitter = socials.find { it.name == "Twitter" }?.link
    val github = socials.find { it.name == "GitHub" }?.link
    return SpeakerDb(
        id = id,
        displayName = name,
        pronouns = null,
        bio = bio ?: "",
        jobTitle = jobTitle,
        company = company,
        photoUrl = photoUrl ?: "",
        website = null,
        twitter = if (twitter?.contains("twitter.com") == true) twitter
        else "https://twitter.com/$twitter",
        mastodon = null,
        github = if (github?.contains("github.com") == true) github
        else "https://github.com/$github",
        linkedin = null
    )
}

fun SessionOP.convertToTalkDb() = TalkDb(
    id = id,
    title = title,
    level = level,
    abstract = abstract,
    category = categoryId,
    format = formatId,
    language = language,
    speakerIds = speakerIds,
    linkSlides = null,
    linkReplay = null
)

fun SessionOP.convertToScheduleDb(order: Int) = ScheduleDb(
    order = order,
    startTime = dateStart?.split("+")?.first()
        ?: error("Can't schedule a talk without a start time"),
    endTime = dateEnd?.split("+")?.first()
        ?: error("Can't schedule a talk without a end time"),
    room = trackId ?: error("Can't schedule a talk without a room"),
    talkId = id
)
