package com.paligot.confily.backend.internals.infrastructure.firestore

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import java.text.DecimalFormat
import java.time.LocalDateTime

data class FeaturesActivatedEntity(
    val hasNetworking: Boolean = false
)

data class OpenPlannerConfigurationEntity(
    val eventId: String = "",
    val privateId: String = ""
)

data class BilletWebConfigurationEntity(
    val eventId: String = "",
    val userId: String = "",
    val apiKey: String = ""
)

data class WldConfigurationEntity(
    val appId: String = "",
    val apiKey: String = ""
)

data class TeamGroupEntity(
    val name: String = "",
    val order: Int = 0
)

data class EventEntity(
    val slugId: String = "",
    val year: String = "",
    val openFeedbackId: String? = null,
    val openPlannerConfig: OpenPlannerConfigurationEntity? = null,
    val billetWebConfig: BilletWebConfigurationEntity? = null,
    val wldConfig: WldConfigurationEntity? = null,
    val apiKey: String = "",
    val name: String = "",
    val defaultLanguage: String = "",
    val eventSessionTracks: List<String> = emptyList(),
    val teamGroups: List<TeamGroupEntity> = emptyList(),
    val address: AddressEntity = AddressEntity(),
    val startDate: String = "",
    val endDate: String = "",
    val sponsoringTypes: List<String> = emptyList(),
    val menus: List<LunchMenuEntity> = emptyList(),
    val coc: String = "",
    val features: FeaturesActivatedEntity = FeaturesActivatedEntity(),
    val contactPhone: String? = null,
    val contactEmail: String = "",
    val socials: List<SocialEntity> = emptyList(),
    @Deprecated("use socials property instead")
    val twitterUrl: String? = null,
    @Deprecated("use socials property instead")
    val linkedinUrl: String? = null,
    val faqLink: String? = null,
    val codeOfConductLink: String? = null,
    val published: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
    val eventUpdatedAt: Long = System.currentTimeMillis(),
    val agendaUpdatedAt: Long = System.currentTimeMillis(),
    val partnersUpdatedAt: Long = System.currentTimeMillis()
)

fun EventEntity.openFeedbackUrl(): String? {
    if (this.openFeedbackId == null) return null
    return try {
        val startDate = LocalDateTime.parse(this.startDate.dropLast(1))
        val formatter = DecimalFormat("00")
        val formatMonth = formatter.format(startDate.monthValue)
        val formatDay = formatter.format(startDate.dayOfMonth)
        val dateFormatted = "${startDate.year}-$formatMonth-$formatDay"
        "https://${SystemEnv.openFeedbackUrl}/${this.openFeedbackId}/$dateFormatted"
    } catch (ignored: Throwable) {
        null
    }
}
