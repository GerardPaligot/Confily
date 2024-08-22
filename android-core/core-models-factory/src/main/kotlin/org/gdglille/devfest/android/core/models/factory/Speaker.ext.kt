package org.gdglille.devfest.android.core.models.factory

import org.gdglille.devfest.models.Speaker

fun Speaker.Companion.builder(): SpeakerBuilder = SpeakerBuilder()

@Suppress("TooManyFunctions")
class SpeakerBuilder {
    private var id: String = ""
    private var displayName: String = ""
    private var bio: String = ""
    private var company: String? = null
    private var photoUrl: String = ""
    private var pronouns: String? = null
    private var jobTitle: String? = null
    private var website: String? = null
    private var twitter: String? = null
    private var linkedin: String? = null
    private var github: String? = null
    private var mastodon: String? = null

    fun id(id: String) = apply { this.id = id }
    fun displayName(displayName: String) = apply { this.displayName = displayName }
    fun bio(bio: String) = apply { this.bio = bio }
    fun company(company: String) = apply { this.company = company }
    fun photoUrl(photoUrl: String) = apply { this.photoUrl = photoUrl }
    fun pronouns(pronouns: String) = apply { this.pronouns = pronouns }
    fun jobTitle(jobTitle: String) = apply { this.jobTitle = jobTitle }
    fun website(website: String) = apply { this.website = website }
    fun twitter(twitter: String) = apply { this.twitter = twitter }
    fun linkedin(linkedin: String) = apply { this.linkedin = linkedin }
    fun github(github: String) = apply { this.github = github }
    fun mastodon(mastodon: String) = apply { this.mastodon = mastodon }

    fun build(): Speaker = Speaker(
        id = id,
        displayName = displayName,
        bio = bio,
        company = company,
        photoUrl = photoUrl,
        pronouns = pronouns,
        jobTitle = jobTitle,
        website = website,
        twitter = twitter,
        linkedin = linkedin,
        github = github,
        mastodon = mastodon
    )
}
