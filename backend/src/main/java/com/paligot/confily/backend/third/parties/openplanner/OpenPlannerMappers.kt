@file:Suppress("TooManyFunctions")

package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.categories.CategoryDb
import com.paligot.confily.backend.formats.FormatDb
import com.paligot.confily.backend.internals.socials.SocialDb
import com.paligot.confily.backend.qanda.AcronymDb
import com.paligot.confily.backend.qanda.QAndAActionDb
import com.paligot.confily.backend.qanda.QAndADb
import com.paligot.confily.backend.schedules.ScheduleDb
import com.paligot.confily.backend.sessions.EventSessionDb
import com.paligot.confily.backend.sessions.TalkDb
import com.paligot.confily.backend.speakers.SpeakerDb
import com.paligot.confily.backend.team.TeamDb
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.inputs.ValidatorException

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
    val linkedin = socials.find { it.name.lowercase() == SocialType.LinkedIn.name.lowercase() }
        ?.link
    val x = socials.find { it.name.lowercase() == SocialType.X.name.lowercase() }?.link
    val twitter = socials.find { it.name.lowercase() == "twitter" }?.link
    val mastodon = socials.find { it.name.lowercase() == SocialType.Mastodon.name.lowercase() }
        ?.link
    val bluesky = socials.find { it.name.lowercase() == SocialType.Bluesky.name.lowercase() }?.link
    val facebook = socials.find { it.name.lowercase() == SocialType.Facebook.name.lowercase() }
        ?.link
    val instagram = socials.find { it.name.lowercase() == SocialType.Instagram.name.lowercase() }
        ?.link
    val youtube = socials.find { it.name.lowercase() == SocialType.YouTube.name.lowercase() }?.link
    val github = socials.find { it.name.lowercase() == SocialType.GitHub.name.lowercase() }?.link
    val email = socials.find { it.name.lowercase() == SocialType.Email.name.lowercase() }?.link
    val website = socials.find { it.name.lowercase() == SocialType.Website.name.lowercase() }?.link
    return SpeakerDb(
        id = id,
        displayName = name,
        pronouns = pronouns,
        bio = bio ?: "",
        email = email,
        jobTitle = jobTitle,
        company = company,
        photoUrl = photoUrl ?: "",
        socials = arrayListOf<SocialDb>().apply {
            if (linkedin?.contains("linkedin.com") == true) {
                this.add(SocialDb(SocialType.LinkedIn.name.lowercase(), linkedin))
            } else if (linkedin != null) {
                val linkedInUrl = "https://linkedin.com/in/$linkedin"
                this.add(SocialDb(SocialType.LinkedIn.name.lowercase(), linkedInUrl))
            }
            if (x?.contains("x.com") == true) {
                this.add(SocialDb(SocialType.X.name.lowercase(), x))
            } else if (x != null) {
                this.add(SocialDb(SocialType.X.name.lowercase(), "https://x.com/$x"))
            }
            if (twitter?.contains("twitter.com") == true) {
                this.add(SocialDb(SocialType.X.name.lowercase(), twitter))
            } else if (twitter != null) {
                this.add(SocialDb(SocialType.X.name.lowercase(), "https://x.com/$twitter"))
            }
            if (mastodon != null) {
                this.add(SocialDb(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (bluesky != null) {
                this.add(SocialDb(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (facebook != null) {
                this.add(SocialDb(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (instagram != null) {
                this.add(SocialDb(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (youtube != null) {
                this.add(SocialDb(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (github?.contains("github.com") == true) {
                this.add(SocialDb(SocialType.GitHub.name.lowercase(), github))
            } else if (github != null) {
                val gitHubUrl = "https://github.com/$github"
                this.add(SocialDb(SocialType.GitHub.name.lowercase(), gitHubUrl))
            }
            if (email != null) {
                this.add(SocialDb(SocialType.Email.name.lowercase(), email))
            }
            if (website != null) {
                this.add(SocialDb(SocialType.Website.name.lowercase(), website))
            }
        }
    )
}

fun SpeakerDb.mergeWith(photoUrl: String?, speakerOP: SpeakerOP): SpeakerDb {
    val linkedin = speakerOP.socials.find { it.name.lowercase() == "linkedin" }?.link
    val x = speakerOP.socials.find { it.name.lowercase() == "x" }?.link
    val twitter = speakerOP.socials.find { it.name.lowercase() == "twitter" }?.link
    val mastodon = speakerOP.socials.find { it.name.lowercase() == "mastodon" }?.link
    val bluesky = speakerOP.socials.find { it.name.lowercase() == "bluesky" }?.link
    val facebook = speakerOP.socials.find { it.name.lowercase() == "facebook" }?.link
    val instagram = speakerOP.socials.find { it.name.lowercase() == "instagram" }?.link
    val youtube = speakerOP.socials.find { it.name.lowercase() == "youtube" }?.link
    val github = speakerOP.socials.find { it.name.lowercase() == "github" }?.link
    val email = speakerOP.socials.find { it.name.lowercase() == "email" }?.link
    val website = speakerOP.socials.find { it.name.lowercase() == "website" }?.link
    return SpeakerDb(
        id = speakerOP.id,
        displayName = if (this.displayName == speakerOP.name) this.displayName else speakerOP.name,
        pronouns = if (this.pronouns == speakerOP.pronouns) this.pronouns else speakerOP.pronouns,
        bio = if (this.bio == speakerOP.bio) this.bio else speakerOP.bio ?: "",
        email = if (this.email == speakerOP.email) this.email else speakerOP.email,
        jobTitle = if (this.jobTitle == speakerOP.jobTitle) this.jobTitle else speakerOP.jobTitle,
        company = if (this.company == speakerOP.company) this.company else speakerOP.company,
        photoUrl = if (this.photoUrl == photoUrl) this.photoUrl else photoUrl ?: "",
        socials = socials.toMutableList().apply {
            if (find { it.type == SocialType.LinkedIn.name.lowercase() } == null && linkedin != null) {
                add(SocialDb(SocialType.LinkedIn.name.lowercase(), linkedin))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && x != null) {
                add(SocialDb(SocialType.X.name.lowercase(), x))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && twitter != null) {
                add(SocialDb(SocialType.X.name.lowercase(), twitter))
            }
            if (find { it.type == SocialType.Mastodon.name.lowercase() } == null && mastodon != null) {
                add(SocialDb(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (find { it.type == SocialType.Bluesky.name.lowercase() } == null && bluesky != null) {
                add(SocialDb(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (find { it.type == SocialType.Facebook.name.lowercase() } == null && facebook != null) {
                add(SocialDb(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (find { it.type == SocialType.Instagram.name.lowercase() } == null && instagram != null) {
                add(SocialDb(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (find { it.type == SocialType.YouTube.name.lowercase() } == null && youtube != null) {
                add(SocialDb(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (find { it.type == SocialType.GitHub.name.lowercase() } == null && github != null) {
                add(SocialDb(SocialType.GitHub.name.lowercase(), github))
            }
            if (find { it.type == SocialType.Email.name.lowercase() } == null && email != null) {
                add(SocialDb(SocialType.Email.name.lowercase(), email))
            }
            if (find { it.type == SocialType.Website.name.lowercase() } == null && website != null) {
                add(SocialDb(SocialType.Website.name.lowercase(), website))
            }
        }
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
    description = if (description == sessionOP.abstract) description else sessionOP.abstract,
    address = address
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

fun FaqItemOP.convertToQAndADb(order: Int, language: String): QAndADb {
    val links = Regex("\\[(.*?)\\]\\((.*?)\\)").findAll(answer)
    var response = answer
    links.forEach {
        response = answer.replaceRange(it.range, it.groupValues[1])
    }
    val acronyms = Regex("\n\\*\\[(.*?)\\]: (.*)").findAll(answer)
    acronyms.forEach {
        response = answer.replaceRange(it.range, "")
    }
    return QAndADb(
        id = id,
        order = order,
        language = language,
        question = question,
        response = response.replace("\n$".toRegex(), ""),
        actions = links
            .mapIndexed { index, matchResult ->
                val (text, url) = matchResult.destructured
                QAndAActionDb(index, text, url)
            }
            .toList(),
        acronyms = acronyms
            .map { matchResult ->
                val (acronym, definition) = matchResult.destructured
                AcronymDb(acronym, definition)
            }
            .toList()
    )
}

fun TeamOP.convertToTeamDb(photoUrl: String?): TeamDb {
    val linkedin = socials.find { it.name.lowercase() == SocialType.LinkedIn.name.lowercase() }
        ?.link
    val x = socials.find { it.name.lowercase() == SocialType.X.name.lowercase() }?.link
    val twitter = socials.find { it.name.lowercase() == "twitter" }?.link
    val mastodon = socials.find { it.name.lowercase() == SocialType.Mastodon.name.lowercase() }
        ?.link
    val bluesky = socials.find { it.name.lowercase() == SocialType.Bluesky.name.lowercase() }?.link
    val facebook = socials.find { it.name.lowercase() == SocialType.Facebook.name.lowercase() }
        ?.link
    val instagram = socials.find { it.name.lowercase() == SocialType.Instagram.name.lowercase() }
        ?.link
    val youtube = socials.find { it.name.lowercase() == SocialType.YouTube.name.lowercase() }?.link
    val github = socials.find { it.name.lowercase() == SocialType.GitHub.name.lowercase() }?.link
    val email = socials.find { it.name.lowercase() == SocialType.Email.name.lowercase() }?.link
    val website = socials.find { it.name.lowercase() == SocialType.Website.name.lowercase() }?.link
    return TeamDb(
        id = id,
        name = name,
        bio = bio ?: "",
        photoUrl = photoUrl,
        role = role,
        socials = arrayListOf<SocialDb>().apply {
            if (linkedin?.contains("linkedin.com") == true) {
                this.add(SocialDb(SocialType.LinkedIn.name.lowercase(), linkedin))
            } else if (linkedin != null) {
                val linkedInUrl = "https://linkedin.com/in/$linkedin"
                this.add(SocialDb(SocialType.LinkedIn.name.lowercase(), linkedInUrl))
            }
            if (x?.contains("x.com") == true) {
                this.add(SocialDb(SocialType.X.name.lowercase(), x))
            } else if (x != null) {
                this.add(SocialDb(SocialType.X.name.lowercase(), "https://x.com/$x"))
            }
            if (twitter?.contains("twitter.com") == true) {
                this.add(SocialDb(SocialType.X.name.lowercase(), twitter))
            } else if (twitter != null) {
                this.add(SocialDb(SocialType.X.name.lowercase(), "https://x.com/$twitter"))
            }
            if (mastodon != null) {
                this.add(SocialDb(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (bluesky != null) {
                this.add(SocialDb(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (facebook != null) {
                this.add(SocialDb(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (instagram != null) {
                this.add(SocialDb(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (youtube != null) {
                this.add(SocialDb(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (github?.contains("github.com") == true) {
                this.add(SocialDb(SocialType.GitHub.name.lowercase(), github))
            } else if (github != null) {
                val gitHubUrl = "https://github.com/$github"
                this.add(SocialDb(SocialType.GitHub.name.lowercase(), gitHubUrl))
            }
            if (email != null) {
                this.add(SocialDb(SocialType.Email.name.lowercase(), email))
            }
            if (website != null) {
                this.add(SocialDb(SocialType.Website.name.lowercase(), website))
            }
        }
    )
}

fun TeamDb.mergeWith(photoUrl: String?, teamOP: TeamOP): TeamDb {
    val linkedin = teamOP.socials.find { it.name.lowercase() == "linkedin" }?.link
    val x = teamOP.socials.find { it.name.lowercase() == "x" }?.link
    val twitter = teamOP.socials.find { it.name.lowercase() == "twitter" }?.link
    val mastodon = teamOP.socials.find { it.name.lowercase() == "mastodon" }?.link
    val bluesky = teamOP.socials.find { it.name.lowercase() == "bluesky" }?.link
    val facebook = teamOP.socials.find { it.name.lowercase() == "facebook" }?.link
    val instagram = teamOP.socials.find { it.name.lowercase() == "instagram" }?.link
    val youtube = teamOP.socials.find { it.name.lowercase() == "youtube" }?.link
    val github = teamOP.socials.find { it.name.lowercase() == "github" }?.link
    val email = teamOP.socials.find { it.name.lowercase() == "email" }?.link
    val website = teamOP.socials.find { it.name.lowercase() == "website" }?.link
    return TeamDb(
        id = teamOP.id,
        name = if (this.name == teamOP.name) this.name else teamOP.name,
        bio = if (this.bio == teamOP.bio) this.bio else teamOP.bio ?: "",
        role = if (this.role == teamOP.role) this.role else teamOP.role,
        photoUrl = if (this.photoUrl == photoUrl) this.photoUrl else photoUrl,
        socials = socials.toMutableList().apply {
            if (find { it.type == SocialType.LinkedIn.name.lowercase() } == null && linkedin != null) {
                add(SocialDb(SocialType.LinkedIn.name.lowercase(), linkedin))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && x != null) {
                add(SocialDb(SocialType.X.name.lowercase(), x))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && twitter != null) {
                add(SocialDb(SocialType.X.name.lowercase(), twitter))
            }
            if (find { it.type == SocialType.Mastodon.name.lowercase() } == null && mastodon != null) {
                add(SocialDb(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (find { it.type == SocialType.Bluesky.name.lowercase() } == null && bluesky != null) {
                add(SocialDb(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (find { it.type == SocialType.Facebook.name.lowercase() } == null && facebook != null) {
                add(SocialDb(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (find { it.type == SocialType.Instagram.name.lowercase() } == null && instagram != null) {
                add(SocialDb(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (find { it.type == SocialType.YouTube.name.lowercase() } == null && youtube != null) {
                add(SocialDb(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (find { it.type == SocialType.GitHub.name.lowercase() } == null && github != null) {
                add(SocialDb(SocialType.GitHub.name.lowercase(), github))
            }
            if (find { it.type == SocialType.Email.name.lowercase() } == null && email != null) {
                add(SocialDb(SocialType.Email.name.lowercase(), email))
            }
            if (find { it.type == SocialType.Website.name.lowercase() } == null && website != null) {
                add(SocialDb(SocialType.Website.name.lowercase(), website))
            }
        }
    )
}
