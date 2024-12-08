package com.paligot.confily.core.models.factory

import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.Speaker

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
    private var socials: MutableList<SocialItem> = mutableListOf()

    fun id(id: String) = apply { this.id = id }
    fun displayName(displayName: String) = apply { this.displayName = displayName }
    fun bio(bio: String) = apply { this.bio = bio }
    fun company(company: String) = apply { this.company = company }
    fun photoUrl(photoUrl: String) = apply { this.photoUrl = photoUrl }
    fun pronouns(pronouns: String) = apply { this.pronouns = pronouns }
    fun jobTitle(jobTitle: String) = apply { this.jobTitle = jobTitle }
    fun website(website: String) = apply { socials.add(SocialItem(SocialType.Website, website)) }
    fun x(x: String) = apply { socials.add(SocialItem(SocialType.X, x)) }
    fun linkedin(linkedin: String) = apply {
        socials.add(SocialItem(SocialType.LinkedIn, linkedin))
    }

    fun github(github: String) = apply { socials.add(SocialItem(SocialType.GitHub, github)) }
    fun mastodon(mastodon: String) = apply {
        socials.add(SocialItem(SocialType.Mastodon, mastodon))
    }

    fun build(): Speaker = Speaker(
        id = id,
        displayName = displayName,
        bio = bio,
        company = company,
        photoUrl = photoUrl,
        pronouns = pronouns,
        jobTitle = jobTitle,
        socials = socials
    )
}
