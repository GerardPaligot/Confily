package com.paligot.confily.schedules.ui.models

data class SpeakerItemUi(
    val id: String,
    val displayName: String,
    val activity: String,
    val photoUrl: String
) {
    companion object {
        val fake = SpeakerItemUi(
            id = "1",
            displayName = "Gérard Paligot",
            activity = "Staff Engineer - Decathlon",
            photoUrl = "https://pbs.twimg.com/profile_images/1790809511353110528/DDHZYoEB_400x400.jpg"
        )
    }
}
