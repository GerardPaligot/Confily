package org.gdglille.devfest.models

data class SpeakerItemUi(
    val id: String,
    val name: String,
    val pronouns: String?,
    val company: String,
    val url: String
) {
    companion object {
        val fake = SpeakerItemUi(
            id = "1",
            name = "Gérard Paligot",
            pronouns = "He/Him",
            company = "Staff Engineer - Decathlon",
            url = "https://pbs.twimg.com/profile_images/1465658195767136257/zdYQWsTj_400x400.jpg"
        )
    }
}
