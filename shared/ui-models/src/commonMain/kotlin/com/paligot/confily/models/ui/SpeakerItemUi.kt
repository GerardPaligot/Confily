package com.paligot.confily.models.ui

data class SpeakerItemUi(
    val id: String,
    val name: String,
    val activity: String,
    val url: String
) {
    companion object {
        val fake = SpeakerItemUi(
            id = "1",
            name = "Gérard Paligot",
            activity = "Staff Engineer - Decathlon",
            url = "https://pbs.twimg.com/profile_images/1790809511353110528/DDHZYoEB_400x400.jpg"
        )
    }
}
