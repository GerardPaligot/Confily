package com.paligot.conferences.repositories

data class AgendaUi(val talks: Map<String, List<TalkItemUi>>)

data class TalkItemUi(
    val id: String,
    val time: String,
    val room: String,
    val title: String,
    val speakers: List<String>,
    val isFavorite: Boolean
)

data class TalkUi(
    val title: String,
    val date: String,
    val room: String,
    val level: String?,
    val abstract: String,
    val speakers: List<SpeakerItemUi>
)

data class SpeakerItemUi(
    val id: String,
    val name: String,
    val company: String,
    val url: String
)

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

data class EventUi(
    val eventInfo: EventInfoUi,
    val partners: PartnerGroupsUi
)

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

data class PartnerGroupsUi(
    val golds: List<PartnerItemUi>,
    val silvers: List<PartnerItemUi>,
    val bronzes: List<PartnerItemUi>,
    val others: List<PartnerItemUi>,
)

data class PartnerItemUi(
    val logoUrl: String,
    val siteUrl: String?,
    val name: String
)
