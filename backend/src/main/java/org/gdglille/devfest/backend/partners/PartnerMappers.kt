package org.gdglille.devfest.backend.partners

import com.google.cloud.Timestamp
import org.gdglille.devfest.models.Partner
import org.gdglille.devfest.models.inputs.PartnerInput

fun PartnerDb.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl
)

fun PartnerInput.convertToDb(id: String? = null) = PartnerDb(
    id = id ?: "",
    logoUrl = logoUrl,
    siteUrl = siteUrl,
    name = name,
    sponsoring = sponsoring,
    creationDate = Timestamp.now()
)
