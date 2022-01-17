package com.paligot.conferences.backend.partners

import com.paligot.conferences.models.Partner

fun PartnerDb.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl
)