package org.gdglille.devfest.backend.events

import java.text.DecimalFormat
import java.time.LocalDateTime

data class AddressDb(
    val formatted: List<String> = emptyList(),
    val address: String = "",
    val country: String = "",
    val countryCode: String = "",
    val city: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class AcronymDb(
    val key: String = "",
    val value: String = ""
)

data class QuestionAndResponseActionDb(
    val order: Int = 0,
    val label: String = "",
    val url: String = ""
)

data class QuestionAndResponseDb(
    val order: Int = 0,
    val question: String = "",
    val response: String = "",
    val actions: List<QuestionAndResponseActionDb> = emptyList(),
    val acronyms: List<AcronymDb> = emptyList()
)

data class LunchMenuDb(
    val name: String = "",
    val dish: String = "",
    val accompaniment: String = "",
    val dessert: String = ""
)

data class CategoryDb(
    val name: String = "",
    val color: String = "",
    val icon: String = ""
)

data class FeaturesActivatedDb(
    val hasNetworking: Boolean = false
)

data class ConferenceHallConfigurationDb(
    val eventId: String = "",
    val apiKey: String = ""
)

data class BilletWebConfigurationDb(
    val eventId: String = "",
    val userId: String = "",
    val apiKey: String = "",
)

data class WldConfigurationDb(
    val appId: String = "",
    val apiKey: String = ""
)

data class EventDb(
    val slugId: String = "",
    val year: String = "",
    val openFeedbackId: String? = null,
    val conferenceHallConfig: ConferenceHallConfigurationDb? = null,
    val billetWebConfig: BilletWebConfigurationDb? = null,
    val wldConfig: WldConfigurationDb? = null,
    val apiKey: String = "",
    val name: String = "",
    val address: AddressDb = AddressDb(),
    val startDate: String = "",
    val endDate: String = "",
    val sponsoringTypes: List<String> = emptyList(),
    val formats: Map<String, Int> = emptyMap(),
    val menus: List<LunchMenuDb> = emptyList(),
    val qanda: List<QuestionAndResponseDb> = emptyList(),
    val coc: String = "",
    val categories: List<CategoryDb> = emptyList(),
    val features: FeaturesActivatedDb = FeaturesActivatedDb(),
    val contactPhone: String? = null,
    val contactEmail: String = "",
    val twitterUrl: String? = null,
    val linkedinUrl: String? = null,
    val faqLink: String? = null,
    val codeOfConductLink: String? = null,
    val updatedAt: Long = System.currentTimeMillis(),
    val agendaUpdatedAt: Long = System.currentTimeMillis()
)

fun EventDb.openFeedbackUrl(): String? {
    if (this.openFeedbackId == null) return null
    return try {
        val startDate = LocalDateTime.parse(this.startDate.dropLast(1))
        val formatter = DecimalFormat("00")
        val formatMonth = formatter.format(startDate.monthValue)
        val formatDay = formatter.format(startDate.dayOfMonth)
        val dateFormatted = "${startDate.year}-$formatMonth-$formatDay"
        "https://openfeedback.io/${this.openFeedbackId}/$dateFormatted"
    } catch (ignored: Throwable) {
        null
    }
}
