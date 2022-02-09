package com.paligot.conferences.models

data class EventInfoUi(
    val name: String,
    val address: String,
    val date: String,
    val twitter: String?,
    val twitterUrl: String?,
    val linkedin: String?,
    val linkedinUrl: String?,
    val faqLink: String,
    val codeOfConductLink: String,
)