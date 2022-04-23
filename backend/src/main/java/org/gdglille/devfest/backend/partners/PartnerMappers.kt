package org.gdglille.devfest.backend.partners

import org.gdglille.devfest.models.Partner

fun PartnerDb.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl
)