package com.paligot.conferences.models

data class SpeakerItemUi(
    val id: String,
    val name: String,
    val company: String,
    val url: String,
    val twitter: String?
) {
    companion object {
        val fake = SpeakerItemUi(
            id = "1",
            name = "Gérard Paligot",
            company = "Decathlon",
            url = "https://pbs.twimg.com/profile_images/1465658195767136257/zdYQWsTj_400x400.jpg",
            twitter = null
        )
    }
}
