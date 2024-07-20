package org.gdglille.devfest.models.ui

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
            url = "https://pbs.twimg.com/profile_images/1790809511353110528/DDHZYoEB_400x400.jpg"
        )
    }
}
