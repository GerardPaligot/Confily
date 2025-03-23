package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class ExportEvent(
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
