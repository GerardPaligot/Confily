package com.paligot.confily.models

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
    val id: String,
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
    val endDate: String
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

@Serializable
data class EventV3(
    val id: String,
    val name: String,
    val address: Address,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val menus: List<EventLunchMenu>,
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

@Serializable
data class EventV4(
    val id: String,
    val name: String,
    val address: Address,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val menus: List<EventLunchMenu>,
    val coc: String,
    @SerialName("openfeedback_project_id")
    val openfeedbackProjectId: String?,
    val features: FeaturesActivated,
    @SerialName("contact_phone")
    val contactPhone: String?,
    @SerialName("contact_email")
    val contactEmail: String,
    val socials: List<SocialItem>,
    @SerialName("faq_link")
    val faqLink: String?,
    @SerialName("code_of_conduct_link")
    val codeOfConductLink: String?,
    @SerialName("updated_at")
    val updatedAt: Long
)

@Serializable
data class EventV5(
    val id: String,
    val name: String,
    val address: Address,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    val contact: EventContact,
    val coc: CodeOfConduct,
    val qanda: QAndA,
    val menus: List<EventLunchMenu>,
    val features: FeaturesActivated,
    val team: Map<String, List<TeamMember>>,
    val maps: List<EventMap>,
    @SerialName("third_party")
    val thirdParty: ThirdParty,
    @SerialName("updated_at")
    val updatedAt: Long
)

@Serializable
data class EventContact(
    val phone: String?,
    val email: String?,
    val socials: List<SocialItem>
)

@Serializable
data class CodeOfConduct(
    val content: String?,
    val link: String?
)

@Serializable
data class QAndA(
    val content: Map<String, List<QuestionAndResponse>>,
    val link: String?
)

@Serializable
data class ThirdParty(
    @SerialName("openfeedback_project_id")
    val openfeedbackProjectId: String?
)
