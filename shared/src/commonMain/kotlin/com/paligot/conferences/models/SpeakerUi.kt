package com.paligot.conferences.models

data class SpeakerUi(
    val url: String,
    val name: String,
    val company: String,
    val bio: String,
    val twitter: String?,
    val twitterUrl: String?,
    val github: String?,
    val githubUrl: String?
)