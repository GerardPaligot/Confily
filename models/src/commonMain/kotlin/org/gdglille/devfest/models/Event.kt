package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Acronym(
    val key: String,
    val value: String
)

@Serializable
data class QuestionAndResponseAction(
    val order: Int,
    val label: String,
    val url: String
)

@Serializable
data class QuestionAndResponse(
    val order: Int,
    val question: String,
    val response: String,
    val actions: List<QuestionAndResponseAction>,
    val acronyms: List<Acronym>
)

@Serializable
data class EventLunchMenu(
    val name: String,
    val dish: String,
    val accompaniment: String,
    val dessert: String
)

@Serializable
data class EventPartners(
    val golds: List<Partner>,
    val silvers: List<Partner>,
    val bronzes: List<Partner>,
    val others: List<Partner>
)

@Serializable
data class FeaturesActivated(
    @SerialName("has_networking")
    val hasNetworking: Boolean,
    @SerialName("has_speaker_list")
    val hasSpeakerList: Boolean,
    @SerialName("has_partner_list")
    val hasPartnerList: Boolean,
    @SerialName("has_menus")
    val hasMenus: Boolean,
    @SerialName("has_qanda")
    val hasQAndA: Boolean,
    @SerialName("has_billet_web_ticket")
    val hasBilletWebTicket: Boolean
)

@Serializable
data class Event(
    val id: String,
    val name: String,
    val address: Address,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val partners: EventPartners,
    val menus: List<EventLunchMenu>,
    val qanda: List<QuestionAndResponse>,
    val coc: String,
    val features: FeaturesActivated,
    @SerialName("twitter_url")
    val twitterUrl: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("faq_link")
    val faqLink: String?,
    @SerialName("code_of_conduct_link")
    val codeOfConductLink: String?,
    @SerialName("updated_at")
    val updatedAt: Long
)

@Serializable
data class EventList(
    val future: List<EventItemList>,
    val past: List<EventItemList>
)

@Serializable
data class EventItemList(
    val id: String,
    val name: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
)

@Serializable
data class EventV2(
    val id: String,
    val name: String,
    val address: Address,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val menus: List<EventLunchMenu>,
    val qanda: List<QuestionAndResponse>,
    val coc: String,
    @SerialName("openfeedback_project_id")
    val openfeedbackProjectId: String?,
    val features: FeaturesActivated,
    @SerialName("contact_phone")
    val contactPhone: String?,
    @SerialName("contact_email")
    val contactEmail: String,
    @SerialName("twitter_url")
    val twitterUrl: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("faq_link")
    val faqLink: String?,
    @SerialName("code_of_conduct_link")
    val codeOfConductLink: String?,
    @SerialName("updated_at")
    val updatedAt: Long
)
