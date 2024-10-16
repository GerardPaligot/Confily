package com.paligot.confily.backend.speakers

import com.paligot.confily.models.Speaker
import com.paligot.confily.models.inputs.SpeakerInput

fun com.paligot.confily.backend.third.parties.conferencehall.Speaker.convertToDb(url: String): SpeakerDb =
    SpeakerDb(
        id = this.uid,
        displayName = this.displayName,
        bio = this.bio ?: "",
        company = this.company,
        photoUrl = url,
        twitter = if (this.twitter == null) {
            null
        } else if (this.twitter.contains("twitter.com")) {
            this.twitter
        } else {
            "https://twitter.com/${this.twitter}"
        },
        github = if (this.github == null) {
            null
        } else if (this.github.contains("github.com")) {
            this.github
        } else {
            "https://github.com/${this.github}"
        },
        linkedin = null
    )

fun SpeakerDb.convertToModel(): Speaker = Speaker(
    id = this.id,
    displayName = this.displayName,
    pronouns = this.pronouns,
    bio = this.bio,
    jobTitle = this.jobTitle,
    company = this.company,
    photoUrl = this.photoUrl,
    website = this.website,
    twitter = this.twitter,
    mastodon = this.mastodon,
    github = this.github,
    linkedin = this.linkedin
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
    website = this.website,
    twitter = this.twitter,
    mastodon = this.mastodon,
    github = this.github,
    linkedin = this.linkedin
)
