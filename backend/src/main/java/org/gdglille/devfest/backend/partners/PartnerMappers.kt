package org.gdglille.devfest.backend.partners

import com.google.cloud.Timestamp
import org.gdglille.devfest.backend.events.AddressDb
import org.gdglille.devfest.backend.events.convertToModel
import org.gdglille.devfest.backend.internals.helpers.storage.Upload
import org.gdglille.devfest.models.Job
import org.gdglille.devfest.models.Partner
import org.gdglille.devfest.models.PartnerMedia
import org.gdglille.devfest.models.PartnerMediaPngs
import org.gdglille.devfest.models.PartnerV2
import org.gdglille.devfest.models.inputs.PartnerInput
import java.net.URI

fun PartnerDb.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl
)

fun PartnerDb.convertToPartnerMediaModel(): PartnerMedia {
    if (media != null) {
        return PartnerMedia(
            svg = media.svg,
            pngs = PartnerMediaPngs(
                _250 = media.pngs._250,
                _500 = media.pngs._500,
                _1000 = media.pngs._1000
            )
        )
    }
    val uri = URI.create(this.logoUrl)
    val path = this.logoUrl.split("?")[0]
    val pngs = if (path.endsWith(".svg")) {
        PartnerMediaPngs(
            _250 = "${path.replace(".svg", "-250.png")}?${uri.query}",
            _500 = "${path.replace(".svg", "-500.png")}?${uri.query}",
            _1000 = "${path.replace(".svg", "-1000.png")}?${uri.query}"
        )
    } else {
        PartnerMediaPngs(
            _250 = "$path-250.png?${uri.query}",
            _500 = "$path-500.png?${uri.query}",
            _1000 = "$path-1000.png?${uri.query}"
        )
    }
    return PartnerMedia(
        svg = this.logoUrl,
        pngs = pngs
    )
}

fun PartnerDb.convertToModelV2(jobs: List<Job>) = PartnerV2(
    id = this.id,
    name = this.name,
    description = this.description,
    logoUrl = this.logoUrl,
    media = convertToPartnerMediaModel(),
    siteUrl = this.siteUrl,
    twitterUrl = this.twitterUrl,
    twitterMessage = this.twitterMessage,
    linkedinUrl = this.linkedinUrl,
    linkedinMessage = this.linkedinMessage,
    address = this.address.convertToModel(),
    jobs = jobs
)

@Suppress("ReturnCount")
fun List<Upload>.convertToPartnerMediaDb(logoUrl: String): PartnerMediaDb? {
    if (isEmpty()) return null
    val image250 = find { it.filename.contains("250.png") } ?: return null
    val image500 = find { it.filename.contains("500.png") } ?: return null
    val image1000 = find { it.filename.contains("1000.png") } ?: return null
    return PartnerMediaDb(
        svg = logoUrl,
        pngs = PartnerPngsDb(
            _250 = image250.url,
            _500 = image500.url,
            _1000 = image1000.url
        )
    )
}

fun PartnerInput.convertToDb(id: String? = null, addressDb: AddressDb, uploads: List<Upload> = emptyList()) = PartnerDb(
    id = id ?: "",
    name = name,
    description = description,
    logoUrl = logoUrl,
    media = uploads.convertToPartnerMediaDb(logoUrl),
    siteUrl = siteUrl,
    twitterUrl = twitterUrl,
    twitterMessage = twitterMessage,
    linkedinUrl = linkedinUrl,
    linkedinMessage = linkedinMessage,
    address = addressDb,
    sponsoring = sponsoring,
    wldId = wldId,
    creationDate = Timestamp.now()
)
