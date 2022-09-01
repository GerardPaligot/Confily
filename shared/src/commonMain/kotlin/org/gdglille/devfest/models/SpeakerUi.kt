package org.gdglille.devfest.models

data class SpeakerUi(
    val url: String,
    val name: String,
    val company: String,
    val bio: String,
    val twitter: String?,
    val twitterUrl: String?,
    val github: String?,
    val githubUrl: String?,
    val talks: List<TalkItemUi>
) {
    companion object {
        val fake = SpeakerUi(
            url = "https://pbs.twimg.com/profile_images/1465658195767136257/zdYQWsTj_400x400.jpg",
            name = "Gérard Paligot",
            company = "Decathlon",
            bio = "Father and husband // Software Staff Engineer at Decathlon // Speaker // LAUG, FRAUG, GDGLille, DevfestLille organizer // Disney Fan!",
            twitter = "GerardPaligot",
            twitterUrl = "https://twitter.com/GerardPaligot",
            github = "GerardPaligot",
            githubUrl = "https://github.com/GerardPaligot",
            talks = arrayListOf(TalkItemUi.fake)
        )
    }
}
