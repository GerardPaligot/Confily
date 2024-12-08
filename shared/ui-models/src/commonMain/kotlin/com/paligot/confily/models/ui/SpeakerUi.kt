package com.paligot.confily.models.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SpeakerUi(
    val url: String,
    val name: String,
    val pronouns: String?,
    val jobTitle: String?,
    val company: String?,
    val activity: String?,
    val bio: String,
    val socials: ImmutableList<SocialUi>,
    val talks: ImmutableList<TalkItemUi>
) {
    companion object {
        val fake = SpeakerUi(
            url = "https://pbs.twimg.com/profile_images/1790809511353110528/DDHZYoEB_400x400.jpg",
            name = "Gérard Paligot",
            pronouns = "He/Him",
            jobTitle = "Staff Engineer",
            company = "Decathlon",
            activity = "Staff Engineer at Decathlon",
            bio = "Father and husband // Software Staff Engineer at Decathlon // Speaker // LAUG, FRAUG, GDGLille, DevfestLille organizer // Disney Fan!",
            socials = persistentListOf(
                SocialUi(type = SocialTypeUi.Website, url = "https://gerard.paligot.com/"),
                SocialUi(type = SocialTypeUi.X, url = "https://x.com/GerardPaligot"),
                SocialUi(
                    type = SocialTypeUi.LinkedIn,
                    url = "https://www.linkedin.com/in/gpaligot/"
                ),
                SocialUi(
                    type = SocialTypeUi.Mastodon,
                    url = "https://androiddev.social/@gerardpaligot"
                )
            ),
            talks = persistentListOf(TalkItemUi.fake)
        )
    }
}
