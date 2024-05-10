package org.gdglille.devfest.backend.third.parties.openplanner

import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.formats.FormatDb
import org.gdglille.devfest.backend.schedules.ScheduleDb
import org.gdglille.devfest.backend.sessions.EventSessionDb
import org.gdglille.devfest.backend.sessions.TalkDb
import org.gdglille.devfest.backend.speakers.SpeakerDb
import org.gdglille.devfest.models.inputs.ValidatorException

fun CategoryOP.convertToDb() = CategoryDb(
    id = id,
    name = name,
    color = "",
    icon = ""
)

fun CategoryDb.mergeWith(category: CategoryOP) = CategoryDb(
    id = category.id,
    name = if (this.name == category.name) this.name else category.name,
    color = if (this.color != "") this.color else category.color,
    icon = if (this.icon != "") this.icon else ""
)

fun FormatOP.convertToDb() = FormatDb(
    id = id,
    name = name,
    time = durationMinutes
)

fun FormatDb.mergeWith(formatOP: FormatOP) = FormatDb(
    id = formatOP.id,
    name = if (this.name == formatOP.name) this.name else formatOP.name,
    time = if (this.time != 0) this.time else formatOP.durationMinutes
)

fun SpeakerOP.convertToDb(photoUrl: String?): SpeakerDb {
    val twitter = socials.find { it.name.lowercase() == "twitter" }?.link
    val github = socials.find { it.name.lowercase() == "github" }?.link
    return SpeakerDb(
        id = id,
        displayName = name,
        pronouns = null,
        bio = bio ?: "",
        email = email,
        jobTitle = jobTitle,
        company = company,
        photoUrl = photoUrl ?: "",
        website = socials.find { it.name.lowercase() == "website" }?.link,
        twitter = if (twitter?.contains("twitter.com") == true) {
            twitter
        } else if (twitter != null) {
            "https://twitter.com/$twitter"
        } else {
            null
        },
        mastodon = socials.find { it.name.lowercase() == "mastodon" }?.link,
        github = if (github?.contains("github.com") == true) {
            github
        } else if (github != null) {
            "https://github.com/$github"
        } else {
            null
        },
        linkedin = socials.find { it.name.lowercase() == "linkedin" }?.link
    )
}

fun SpeakerDb.mergeWith(photoUrl: String?, speakerOP: SpeakerOP): SpeakerDb {
    val twitter = speakerOP.socials.find { it.name.lowercase() == "twitter" }?.link
    val github = speakerOP.socials.find { it.name.lowercase() == "gitHub" }?.link
    val website = speakerOP.socials.find { it.name.lowercase() == "website" }?.link
    val mastodon = speakerOP.socials.find { it.name.lowercase() == "mastodon" }?.link
    val linkedin = speakerOP.socials.find { it.name.lowercase() == "linkedin" }?.link
    return SpeakerDb(
        id = speakerOP.id,
        displayName = if (this.displayName == speakerOP.name) this.displayName else speakerOP.name,
        pronouns = pronouns,
        bio = if (this.bio == speakerOP.bio) this.bio else speakerOP.bio ?: "",
        email = if (this.email == speakerOP.email) this.email else speakerOP.email,
        jobTitle = if (this.jobTitle == speakerOP.jobTitle) this.jobTitle else speakerOP.jobTitle,
        company = if (this.company == speakerOP.company) this.company else speakerOP.company,
        photoUrl = if (this.photoUrl == photoUrl) this.photoUrl else photoUrl ?: "",
        website = if (this.website == website) this.website else website,
        twitter = if (this.twitter == twitter) {
            this.twitter
        } else if (twitter?.contains("twitter.com") == true) {
            twitter
        } else if (twitter != null) {
            "https://twitter.com/$twitter"
        } else {
            null
        },
        mastodon = if (this.mastodon == mastodon) this.mastodon else mastodon,
        github = if (this.github == github) {
            this.github
        } else if (github?.contains("github.com") == true) {
            github
        } else if (github != null) {
            "https://github.com/$github"
        } else {
            null
        },
        linkedin = if (this.linkedin == linkedin) this.linkedin else linkedin
    )
}

fun SessionOP.convertToTalkDb() = TalkDb(
    id = id,
    title = title,
    level = level,
    abstract = abstract ?: throw ValidatorException("Abstract shouldn't be null"),
    category = categoryId ?: throw ValidatorException("Category id shouldn't be null"),
    format = formatId ?: throw ValidatorException("Format id shouldn't be null"),
    language = language ?: throw ValidatorException("Language shouldn't be null"),
    speakerIds = speakerIds,
    linkSlides = null,
    linkReplay = null
)

fun SessionOP.convertToEventSessionDb() = EventSessionDb(
    id = id,
    title = title,
    description = abstract
)

fun TalkDb.mergeWith(sessionOP: SessionOP) = TalkDb(
    id = sessionOP.id,
    title = if (title == sessionOP.title) title else sessionOP.title,
    level = if (level == sessionOP.level) level else sessionOP.level,
    abstract = if (abstract == sessionOP.abstract) {
        abstract
    } else {
        sessionOP.abstract ?: throw ValidatorException("Abstract shouldn't be null")
    },
    category = if (category == sessionOP.categoryId) {
        category
    } else {
        sessionOP.categoryId ?: throw ValidatorException("Category id shouldn't be null")
    },
    format = if (format == sessionOP.formatId) {
        format
    } else {
        sessionOP.formatId ?: throw ValidatorException("Format id shouldn't be null")
    },
    language = if (language == sessionOP.language) {
        language
    } else {
        sessionOP.language ?: throw ValidatorException("Language shouldn't be null")
    },
    speakerIds = if (speakerIds == sessionOP.speakerIds) speakerIds else sessionOP.speakerIds,
    linkSlides = linkSlides,
    linkReplay = linkReplay
)

fun EventSessionDb.mergeWith(sessionOP: SessionOP) = EventSessionDb(
    id = sessionOP.id,
    title = if (title == sessionOP.title) title else sessionOP.title,
    description = if (description == sessionOP.abstract) description else sessionOP.abstract
)

fun SessionOP.convertToScheduleDb(order: Int, tracks: List<TrackOP>) = ScheduleDb(
    order = order,
    startTime = dateStart?.split("+")?.first()
        ?: error("Can't schedule a talk without a start time"),
    endTime = dateEnd?.split("+")?.first()
        ?: error("Can't schedule a talk without a end time"),
    room = trackId?.let { tracks.find { it.id == trackId }?.name }
        ?: error("Can't schedule a talk without a room"),
    talkId = id
)
