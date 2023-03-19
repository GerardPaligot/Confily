package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SpeakerUi(
    val url: String,
    val name: String,
    val pronouns: String?,
    val jobTitle: String?,
    val company: String?,
    val bio: String,
    val websiteUrl: String?,
    val twitterUrl: String?,
    val mastodonUrl: String?,
    val githubUrl: String?,
    val linkedinUrl: String?,
    val talks: ImmutableList<TalkItemUi>
) {
    companion object {
        val fake = SpeakerUi(
            url = "https://pbs.twimg.com/profile_images/1465658195767136257/zdYQWsTj_400x400.jpg",
            name = "Gérard Paligot",
            pronouns = "He/Him",
            jobTitle = "Staff Engineer",
            company = "Decathlon",
            bio = "Father and husband // Software Staff Engineer at Decathlon // Speaker // LAUG, FRAUG, GDGLille, DevfestLille organizer // Disney Fan!",
            websiteUrl = "https://gerard.paligot.com/",
            twitterUrl = "https://twitter.com/GerardPaligot",
            mastodonUrl = "https://androiddev.social/@gerardpaligot",
            githubUrl = "https://github.com/GerardPaligot",
            linkedinUrl = "https://www.linkedin.com/in/gpaligot/",
            talks = persistentListOf(TalkItemUi.fake)
        )
    }
}
