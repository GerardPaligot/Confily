package org.gdglille.devfest.backend.third.parties.cms4partners

import org.gdglille.devfest.models.inputs.PartnerInput

fun WebhookInput.mapToPartnerInput(): PartnerInput {
    val hasTwitter = data.twitterAccount == "" || data.twitterAccount != null
    val hasLinkedIn = data.linkedinAccount == "" || data.linkedinAccount != null
    val hasSchemaSiteUrl = data.siteUrl?.startsWith("https://") ?: false
    return PartnerInput(
        name = data.name,
        description = data.description ?: "",
        logoUrl = data.logoUrl!!,
        siteUrl = if (hasSchemaSiteUrl) data.siteUrl!! else "https://${data.siteUrl}",
        twitterUrl = if (hasTwitter) data.twitterAccount else null,
        twitterMessage = data.twitter ?: "",
        linkedinUrl = if (hasLinkedIn) data.linkedinAccount else null,
        linkedinMessage = data.linkedin ?: "",
        address = data.address ?: "",
        sponsoring = data.sponsoring!!,
        wldId = data.wldId
    )
}
