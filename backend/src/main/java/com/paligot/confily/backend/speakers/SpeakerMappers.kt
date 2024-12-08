package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.internals.socials.SocialDb
import com.paligot.confily.backend.internals.socials.convertToDb
import com.paligot.confily.backend.internals.socials.convertToModel
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.Speaker
import com.paligot.confily.models.inputs.SocialInput
import com.paligot.confily.models.inputs.SpeakerInput

fun com.paligot.confily.backend.third.parties.conferencehall.Speaker.convertToDb(url: String): SpeakerDb =
    SpeakerDb(
        id = this.uid,
        displayName = this.displayName,
        bio = this.bio ?: "",
        company = this.company,
        photoUrl = url,
        socials = arrayListOf<SocialDb>().apply {
            if (twitter?.contains("twitter.com") == true) {
                this.add(SocialDb(SocialType.X.name.lowercase(), twitter))
            } else if (twitter?.contains("x.com") == true) {
                this.add(SocialDb(SocialType.X.name.lowercase(), twitter))
            } else if (twitter != null) {
                this.add(SocialDb(SocialType.X.name.lowercase(), "https://x.com/$twitter"))
            }
            if (github?.contains("github.com") == true) {
                this.add(SocialDb(SocialType.GitHub.name.lowercase(), github))
            } else if (github != null) {
                val gitHubUrl = "https://github.com/$github"
                this.add(SocialDb(SocialType.GitHub.name.lowercase(), gitHubUrl))
            }
        }
    )

fun SpeakerDb.convertToModel(): Speaker = Speaker(
    id = this.id,
    displayName = this.displayName,
    pronouns = this.pronouns,
    bio = this.bio,
    jobTitle = this.jobTitle,
    company = this.company,
    photoUrl = this.photoUrl,
    socials = socials.map(SocialDb::convertToModel).toMutableList().apply {
        if (find { it.type == SocialType.LinkedIn } == null && linkedin != null) {
            this.add(SocialItem(SocialType.LinkedIn, linkedin))
        }
        if (find { it.type == SocialType.X } == null && twitter != null) {
            this.add(SocialItem(SocialType.X, twitter))
        }
        if (find { it.type == SocialType.Mastodon } == null && mastodon != null) {
            this.add(SocialItem(SocialType.Mastodon, mastodon))
        }
        if (find { it.type == SocialType.Website } == null && website != null) {
            this.add(SocialItem(SocialType.Website, website))
        }
        if (find { it.type == SocialType.GitHub } == null && github != null) {
            this.add(SocialItem(SocialType.GitHub, github))
        }
    }.toList()
)

fun SpeakerInput.convertToDb(photoUrl: String, id: String? = null) = SpeakerDb(
    id = id ?: "",
    displayName = this.displayName,
    pronouns = this.pronouns,
    bio = this.bio,
    email = this.email,
    jobTitle = this.jobTitle,
    company = this.company,
    photoUrl = photoUrl,
    socials = socials.map(SocialInput::convertToDb)
)
