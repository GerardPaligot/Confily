package org.gdglille.devfest.backend.speakers

import org.gdglille.devfest.models.Speaker
import org.gdglille.devfest.models.inputs.SpeakerInput

fun org.gdglille.devfest.backend.network.conferencehall.Speaker.convertToDb(url: String): SpeakerDb =
    SpeakerDb(
        id = this.uid,
        displayName = this.displayName,
        bio = this.bio ?: "",
        company = this.company,
        photoUrl = url,
        twitter = if (this.twitter == null) null
        else if (this.twitter.contains("twitter.com")) this.twitter
        else "https://twitter.com/${this.twitter}",
        github = if (this.github == null) null
        else if (this.github.contains("github.com")) this.github
        else "https://github.com/${this.github}",
        linkedin = null
    )

fun SpeakerDb.convertToModel(): Speaker = Speaker(
    id = this.id,
    displayName = this.displayName,
    bio = this.bio,
    company = this.company,
    photoUrl = this.photoUrl,
    twitter = this.twitter,
    github = this.github,
    linkedin = this.linkedin
)

fun SpeakerInput.convertToDb(id: String? = null) = SpeakerDb(
    id = id ?: "",
    displayName = this.displayName,
    bio = this.bio,
    company = this.company,
    photoUrl = this.photoUrl,
    twitter = this.twitter,
    github = this.github,
    linkedin = this.linkedin
)
