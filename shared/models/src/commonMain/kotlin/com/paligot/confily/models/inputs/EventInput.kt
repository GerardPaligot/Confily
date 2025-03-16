package com.paligot.confily.models.inputs

import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LunchMenuInput(
    val name: String,
    val dish: String,
    val accompaniment: String,
    val dessert: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class CoCInput(
    val coc: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class FeaturesActivatedInput(
    @SerialName("has_networking")
    val hasNetworking: Boolean
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class ConferenceHallConfigInput(
    @SerialName("event_id")
    val eventId: String,
    @SerialName("api_key")
    val apiKey: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class OpenPlannerConfigInput(
    @SerialName("event_id")
    val eventId: String,
    @SerialName("private_id")
    val privateId: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class BilletWebConfigInput(
    @SerialName("event_id")
    val eventId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("api_key")
    val apiKey: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class WldConfigInput(
    @SerialName("app_id")
    val appId: String,
    @SerialName("api_key")
    val apiKey: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class TeamGroupInput(
    val name: String,
    val order: Int
) : Validator {
    override fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Name cannot be blank")
        }
        if (order < 0) {
            errors.add("Order cannot be negative")
        }
        return errors
    }
}

@Serializable
data class EventInput(
    val name: String,
    val address: String,
    @SerialName("open_feedback_id")
    val openFeedbackId: String?,
    @SerialName("conference_hall_config")
    val conferenceHallConfigInput: ConferenceHallConfigInput?,
    @SerialName("open_planner_config")
    val openPlannerConfigInput: OpenPlannerConfigInput?,
    @SerialName("billet_web_config")
    val billetWebConfig: BilletWebConfigInput?,
    @SerialName("wld_config")
    val wldConfig: WldConfigInput?,
    @SerialName("event_session_tracks")
    val eventSessionTracks: List<String> = emptyList(),
    @SerialName("team_groups")
    val teamGroups: List<TeamGroupInput> = emptyList(),
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("sponsoring_types")
    val sponsoringTypes: List<String> = emptyList(),
    @SerialName("contact_phone")
    val contactPhone: String?,
    @SerialName("contact_email")
    val contactEmail: String,
    val socials: List<SocialInput> = emptyList(),
    @SerialName("faq_link")
    val faqLink: String,
    val published: Boolean,
    @SerialName("code_of_conduct_link")
    val codeOfConductLink: String,
    @SerialName("update_at")
    val updatedAt: Long = Clock.System.now().epochSeconds
) : Validator {
    override fun validate(): List<String> = emptyList()
}
