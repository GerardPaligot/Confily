package org.gdglille.devfest.backend.partners

import com.google.cloud.Timestamp
import org.gdglille.devfest.backend.events.AddressDb
import org.gdglille.devfest.backend.events.convertToModel
import org.gdglille.devfest.models.Partner
import org.gdglille.devfest.models.PartnerV2
import org.gdglille.devfest.models.inputs.PartnerInput

fun PartnerDb.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl
)

fun PartnerDb.convertToModelV2() = PartnerV2(
    id = this.id,
    name = this.name,
    description = this.description,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl,
    twitterUrl = this.twitterUrl,
    twitterMessage = this.twitterMessage,
    linkedinUrl = this.linkedinUrl,
    linkedinMessage = this.linkedinMessage,
    address = this.address.convertToModel()
)

fun PartnerInput.convertToDb(id: String? = null, addressDb: AddressDb) = PartnerDb(
    id = id ?: "",
    name = name,
    description = description,
    logoUrl = logoUrl,
    siteUrl = siteUrl,
    twitterUrl = twitterUrl,
    twitterMessage = twitterMessage,
    linkedinUrl = linkedinUrl,
    linkedinMessage = linkedinMessage,
    address = addressDb,
    sponsoring = sponsoring,
    creationDate = Timestamp.now()
)
