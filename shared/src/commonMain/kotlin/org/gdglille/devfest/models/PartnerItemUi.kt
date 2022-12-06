package org.gdglille.devfest.models

data class PartnerItemUi(
    val name: String,
    val description: String,
    val logoUrl: String,
    val siteUrl: String?,
    val twitterUrl: String?,
    val twitterMessage: String?,
    val linkedinUrl: String?,
    val linkedinMessage: String?,
    val formattedAddress: List<String>?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?
) {
    companion object {
        val fake = PartnerItemUi(
            name = "WeLoveDevs",
            description = "WeLoveDevs open their doors to the company where you'll be the most happier!",
            logoUrl = "https://devfest-2021-908e1.web.app/img/sponsors/welovedevs.png",
            siteUrl = "https://welovedevs.com/",
            twitterUrl = "https://twitter.com/welovedevs",
            twitterMessage = null,
            linkedinUrl = "https://www.linkedin.com/company/welovedevs/",
            linkedinMessage = null,
            formattedAddress = listOf("165 Av. de Bretagne", "59000 Lille", "France"),
            address = "165 Av. de Bretagne, 59000 Lille, France",
            latitude = 50.6340034,
            longitude = 3.0207872
        )
    }
}
