@file:Suppress("TooManyFunctions")

package com.paligot.confily.backend.third.parties.openplanner.infrastructure.firestore

import com.paligot.confily.backend.categories.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.formats.infrastructure.firestore.FormatEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SocialEntity
import com.paligot.confily.backend.sessions.infrastructure.firestore.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.firestore.TalkSessionEntity
import com.paligot.confily.backend.speakers.infrastructure.firestore.SpeakerEntity
import com.paligot.confily.backend.team.infrastructure.firestore.TeamEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.CategoryOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FormatOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SessionOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SpeakerOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.TeamOP
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.inputs.ValidatorException

fun CategoryEntity.mergeWith(category: CategoryOP) = CategoryEntity(
    id = category.id,
    name = if (this.name == category.name) this.name else category.name,
    color = if (this.color != "") this.color else category.color,
    icon = if (this.icon != "") this.icon else ""
)

fun FormatEntity.mergeWith(formatOP: FormatOP) = FormatEntity(
    id = formatOP.id,
    name = if (this.name == formatOP.name) this.name else formatOP.name,
    time = if (this.time != 0) this.time else formatOP.durationMinutes
)

fun SpeakerEntity.mergeWith(photoUrl: String?, speakerOP: SpeakerOP): SpeakerEntity {
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
    return SpeakerEntity(
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
                add(SocialEntity(SocialType.LinkedIn.name.lowercase(), linkedin))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && x != null) {
                add(SocialEntity(SocialType.X.name.lowercase(), x))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && twitter != null) {
                add(SocialEntity(SocialType.X.name.lowercase(), twitter))
            }
            if (find { it.type == SocialType.Mastodon.name.lowercase() } == null && mastodon != null) {
                add(SocialEntity(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (find { it.type == SocialType.Bluesky.name.lowercase() } == null && bluesky != null) {
                add(SocialEntity(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (find { it.type == SocialType.Facebook.name.lowercase() } == null && facebook != null) {
                add(SocialEntity(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (find { it.type == SocialType.Instagram.name.lowercase() } == null && instagram != null) {
                add(SocialEntity(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (find { it.type == SocialType.YouTube.name.lowercase() } == null && youtube != null) {
                add(SocialEntity(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (find { it.type == SocialType.GitHub.name.lowercase() } == null && github != null) {
                add(SocialEntity(SocialType.GitHub.name.lowercase(), github))
            }
            if (find { it.type == SocialType.Email.name.lowercase() } == null && email != null) {
                add(SocialEntity(SocialType.Email.name.lowercase(), email))
            }
            if (find { it.type == SocialType.Website.name.lowercase() } == null && website != null) {
                add(SocialEntity(SocialType.Website.name.lowercase(), website))
            }
        }
    )
}

fun TalkSessionEntity.mergeWith(sessionOP: SessionOP) = TalkSessionEntity(
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
    linkReplay = linkReplay,
    driveFolderId = driveFolderId
)

fun EventSessionEntity.mergeWith(sessionOP: SessionOP) = EventSessionEntity(
    id = sessionOP.id,
    title = if (title == sessionOP.title) title else sessionOP.title,
    description = if (description == sessionOP.abstract) description else sessionOP.abstract,
    address = address
)

fun TeamEntity.mergeWith(photoUrl: String?, teamOP: TeamOP): TeamEntity {
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
    return TeamEntity(
        id = teamOP.id,
        order = order,
        name = if (this.name == teamOP.name) this.name else teamOP.name,
        bio = if (this.bio == teamOP.bio) this.bio else teamOP.bio ?: "",
        role = if (this.role == teamOP.role) this.role else teamOP.role,
        photoUrl = if (this.photoUrl == photoUrl) this.photoUrl else photoUrl,
        socials = socials.toMutableList().apply {
            if (find { it.type == SocialType.LinkedIn.name.lowercase() } == null && linkedin != null) {
                add(SocialEntity(SocialType.LinkedIn.name.lowercase(), linkedin))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && x != null) {
                add(SocialEntity(SocialType.X.name.lowercase(), x))
            }
            if (find { it.type == SocialType.X.name.lowercase() } == null && twitter != null) {
                add(SocialEntity(SocialType.X.name.lowercase(), twitter))
            }
            if (find { it.type == SocialType.Mastodon.name.lowercase() } == null && mastodon != null) {
                add(SocialEntity(SocialType.Mastodon.name.lowercase(), mastodon))
            }
            if (find { it.type == SocialType.Bluesky.name.lowercase() } == null && bluesky != null) {
                add(SocialEntity(SocialType.Bluesky.name.lowercase(), bluesky))
            }
            if (find { it.type == SocialType.Facebook.name.lowercase() } == null && facebook != null) {
                add(SocialEntity(SocialType.Facebook.name.lowercase(), facebook))
            }
            if (find { it.type == SocialType.Instagram.name.lowercase() } == null && instagram != null) {
                add(SocialEntity(SocialType.Instagram.name.lowercase(), instagram))
            }
            if (find { it.type == SocialType.YouTube.name.lowercase() } == null && youtube != null) {
                add(SocialEntity(SocialType.YouTube.name.lowercase(), youtube))
            }
            if (find { it.type == SocialType.GitHub.name.lowercase() } == null && github != null) {
                add(SocialEntity(SocialType.GitHub.name.lowercase(), github))
            }
            if (find { it.type == SocialType.Email.name.lowercase() } == null && email != null) {
                add(SocialEntity(SocialType.Email.name.lowercase(), email))
            }
            if (find { it.type == SocialType.Website.name.lowercase() } == null && website != null) {
                add(SocialEntity(SocialType.Website.name.lowercase(), website))
            }
        },
        teamName = if (this.teamName == teamOP.team) this.teamName else teamOP.team ?: ""
    )
}
