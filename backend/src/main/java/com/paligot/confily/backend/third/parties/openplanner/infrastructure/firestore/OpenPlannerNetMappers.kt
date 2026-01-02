package com.paligot.confily.backend.third.parties.openplanner.infrastructure.firestore

import com.paligot.confily.backend.categories.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.formats.infrastructure.firestore.FormatEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SocialEntity
import com.paligot.confily.backend.qanda.infrastructure.firestore.AcronymEntity
import com.paligot.confily.backend.qanda.infrastructure.firestore.QAndAActionEntity
import com.paligot.confily.backend.qanda.infrastructure.firestore.QAndAEntity
import com.paligot.confily.backend.schedules.infrastructure.firestore.ScheduleEntity
import com.paligot.confily.backend.sessions.infrastructure.firestore.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.firestore.TalkSessionEntity
import com.paligot.confily.backend.speakers.infrastructure.firestore.SpeakerEntity
import com.paligot.confily.backend.team.infrastructure.firestore.TeamEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.CategoryOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FaqItemOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FormatOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SessionOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SpeakerOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.TeamOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.TrackOP
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.inputs.ValidatorException

fun CategoryOP.convertToEntity() = CategoryEntity(
    id = id,
    name = name,
    color = "",
    icon = ""
)

fun FormatOP.convertToEntity() = FormatEntity(
    id = id,
    name = name,
    time = durationMinutes
)

fun SpeakerOP.convertToEntity(photoUrl: String?): SpeakerEntity {
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
    return SpeakerEntity(
        id = id,
        displayName = name,
        pronouns = pronouns,
        bio = bio ?: "",
        email = email,
        jobTitle = jobTitle,
        company = company,
        photoUrl = photoUrl ?: "",
        socials = arrayListOf<SocialEntity>().apply {
            if (linkedin?.contains("linkedin.com") == true) {
                this.add(SocialEntity(SocialType.LinkedIn.name.lowercase(), linkedin))
            } else if (linkedin != null) {
                val linkedInUrl = "https://linkedin.com/in/$linkedin"
                this.add(SocialEntity(SocialType.LinkedIn.name.lowercase(), linkedInUrl))
            }
            if (x?.contains("x.com") == true) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), x))
            } else if (x != null) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), "https://x.com/$x"))
            }
            if (twitter?.contains("twitter.com") == true) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), twitter))
            } else if (twitter != null) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), "https://x.com/$twitter"))
            }
            if (mastodon != null) {
                this.add(SocialEntity(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (bluesky != null) {
                this.add(SocialEntity(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (facebook != null) {
                this.add(SocialEntity(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (instagram != null) {
                this.add(SocialEntity(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (youtube != null) {
                this.add(SocialEntity(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (github?.contains("github.com") == true) {
                this.add(SocialEntity(SocialType.GitHub.name.lowercase(), github))
            } else if (github != null) {
                val gitHubUrl = "https://github.com/$github"
                this.add(SocialEntity(SocialType.GitHub.name.lowercase(), gitHubUrl))
            }
            if (email != null) {
                this.add(SocialEntity(SocialType.Email.name.lowercase(), email))
            }
            if (website != null) {
                this.add(SocialEntity(SocialType.Website.name.lowercase(), website))
            }
        }
    )
}

fun SessionOP.convertToTalkEntity() = TalkSessionEntity(
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

fun SessionOP.convertToEventSessionEntity() = EventSessionEntity(
    id = id,
    title = title,
    description = abstract
)

fun SessionOP.convertToScheduleEntity(order: Int, tracks: List<TrackOP>) = ScheduleEntity(
    order = order,
    startTime = dateStart?.split("+")?.first()
        ?: error("Can't schedule a talk without a start time"),
    endTime = dateEnd?.split("+")?.first()
        ?: error("Can't schedule a talk without a end time"),
    room = trackId?.let { tracks.find { it.id == trackId }?.name }
        ?: error("Can't schedule a talk without a room"),
    talkId = id
)

fun FaqItemOP.convertToQAndAEntity(order: Int, language: String): QAndAEntity {
    val links = Regex("\\[(.*?)\\]\\((.*?)\\)").findAll(answer)
    var response = answer
    links.forEach {
        response = answer.replaceRange(it.range, it.groupValues[1])
    }
    val acronyms = Regex("\n\\*\\[(.*?)\\]: (.*)").findAll(answer)
    acronyms.forEach {
        response = answer.replaceRange(it.range, "")
    }
    return QAndAEntity(
        id = id,
        order = order,
        language = language,
        question = question,
        response = response.replace("\n$".toRegex(), ""),
        actions = links
            .mapIndexed { index, matchResult ->
                val (text, url) = matchResult.destructured
                QAndAActionEntity(index, text, url)
            }
            .toList(),
        acronyms = acronyms
            .map { matchResult ->
                val (acronym, definition) = matchResult.destructured
                AcronymEntity(acronym, definition)
            }
            .toList()
    )
}

fun TeamOP.convertToTeamEntity(order: Int, photoUrl: String?): TeamEntity {
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
    return TeamEntity(
        id = id,
        order = order,
        name = name,
        bio = bio ?: "",
        photoUrl = photoUrl,
        role = role,
        socials = arrayListOf<SocialEntity>().apply {
            if (linkedin?.contains("linkedin.com") == true) {
                this.add(SocialEntity(SocialType.LinkedIn.name.lowercase(), linkedin))
            } else if (linkedin != null) {
                val linkedInUrl = "https://linkedin.com/in/$linkedin"
                this.add(SocialEntity(SocialType.LinkedIn.name.lowercase(), linkedInUrl))
            }
            if (x?.contains("x.com") == true) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), x))
            } else if (x != null) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), "https://x.com/$x"))
            }
            if (twitter?.contains("twitter.com") == true) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), twitter))
            } else if (twitter != null) {
                this.add(SocialEntity(SocialType.X.name.lowercase(), "https://x.com/$twitter"))
            }
            if (mastodon != null) {
                this.add(SocialEntity(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (bluesky != null) {
                this.add(SocialEntity(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (facebook != null) {
                this.add(SocialEntity(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (instagram != null) {
                this.add(SocialEntity(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (youtube != null) {
                this.add(SocialEntity(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (github?.contains("github.com") == true) {
                this.add(SocialEntity(SocialType.GitHub.name.lowercase(), github))
            } else if (github != null) {
                val gitHubUrl = "https://github.com/$github"
                this.add(SocialEntity(SocialType.GitHub.name.lowercase(), gitHubUrl))
            }
            if (email != null) {
                this.add(SocialEntity(SocialType.Email.name.lowercase(), email))
            }
            if (website != null) {
                this.add(SocialEntity(SocialType.Website.name.lowercase(), website))
            }
        },
        teamName = team ?: ""
    )
}
