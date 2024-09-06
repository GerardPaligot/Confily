package com.paligot.confily.backend.third.parties.cms4partners

import com.paligot.confily.models.inputs.Validator
import kotlinx.serialization.Serializable

@Serializable
data class WebhookInput(
    val id: String,
    val data: PartnerInput
) : Validator {
    override fun validate(): List<String> {
        return mutableListOf<String>().apply {
            if (data.public == false) {
                add("The partner isn't public")
            }
            if (data.status.paid != "done") {
                add("Partner didn't paid yet.")
            }
            if (data.logoUrl == null || data.logoUrl == "") {
                add("Partner doesn't have a logo.")
            }
            if (data.siteUrl == null || data.siteUrl == "") {
                add("Partner doesn't have a website.")
            }
            if (data.sponsoring == null) {
                add("Partner doesn't have a sponsoring.")
            }
        }
    }
}

@Serializable
data class PartnerInput(
    val name: String,
    val public: Boolean? = null,
    val description: String? = null,
    val logoUrl: String? = null,
    val siteUrl: String? = null,
    val twitterAccount: String? = null,
    val twitter: String? = null,
    val linkedinAccount: String? = null,
    val linkedin: String? = null,
    val address: String? = null,
    val sponsoring: String? = null,
    val status: StatusInput,
    val wldId: String? = null
)

@Serializable
data class StatusInput(
    val paid: String? = null
)
