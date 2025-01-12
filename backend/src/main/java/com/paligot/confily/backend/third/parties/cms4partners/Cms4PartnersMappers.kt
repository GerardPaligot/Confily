package com.paligot.confily.backend.third.parties.cms4partners

import com.paligot.confily.models.inputs.PartnerInput
import com.paligot.confily.backend.third.parties.cms4partners.PartnerInput as CmsPartnerInput

fun WebhookInput.mapToPartnerInput(): PartnerInput {
    val hasSchemaSiteUrl = data.siteUrl?.startsWith("https://") ?: false
    return PartnerInput(
        name = data.name,
        description = data.description ?: "",
        logoUrl = data.logoUrl!!,
        videoUrl = null,
        siteUrl = if (hasSchemaSiteUrl) data.siteUrl!! else "https://${data.siteUrl}",
        twitterUrl = data.getTwitterUrl(),
        twitterMessage = data.twitter ?: "",
        linkedinUrl = data.getLinkedInUrl(),
        linkedinMessage = data.linkedin ?: "",
        address = data.address ?: "",
        sponsorings = listOf(data.sponsoring!!),
        wldId = data.wldId
    )
}

fun CmsPartnerInput.getTwitterUrl(): String? {
    return if (twitterAccount != null && twitterAccount != "") {
        if (twitterAccount.contains("twitter.com").not()) {
            "https://twitter.com/$twitterAccount"
        } else {
            twitterAccount
        }
    } else {
        null
    }
}

fun CmsPartnerInput.getLinkedInUrl(): String? {
    return if (linkedinAccount != null && linkedinAccount != "") {
        if (linkedinAccount.contains("linkedin.com").not()) {
            "https://linkedin.com/company/$linkedinAccount"
        } else {
            linkedinAccount
        }
    } else {
        null
    }
}
