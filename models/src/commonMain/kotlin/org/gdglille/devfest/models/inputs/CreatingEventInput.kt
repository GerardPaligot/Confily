package org.gdglille.devfest.models.inputs

import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatingEventInput(
    val name: String,
    val year: String,
    val address: String,
    @SerialName("open_feedback_id")
    val openFeedbackId: String?,
    @SerialName("conference_hall_config")
    val conferenceHallConfigInput: ConferenceHallConfigInput?,
    @SerialName("billet_web_config")
    val billetWebConfig: BilletWebConfigInput?,
    @SerialName("wld_config")
    val wldConfig: WldConfigInput?,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("contact_phone")
    val contactPhone: String?,
    @SerialName("contact_email")
    val contactEmail: String,
    @SerialName("twitter_url")
    val twitterUrl: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("faq_link")
    val faqLink: String,
    @SerialName("code_of_conduct_link")
    val codeOfConductLink: String,
    @SerialName("update_at")
    val updatedAt: Long = Clock.System.now().epochSeconds
) : Validator {
    override fun validate(): List<String> {
        val errors = arrayListOf<String>()
        if (twitterUrl?.contains("twitter.com") == false) errors.add("Your twitter url is malformed")
        if (linkedinUrl?.contains("linkedin.com") == false) errors.add("Your linkedin url is malformed")
        return errors
    }
}
