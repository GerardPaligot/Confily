package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PartnerItemUi(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val siteUrl: String?,
    val twitterUrl: String?,
    val twitterMessage: String?,
    val linkedinUrl: String?,
    val linkedinMessage: String?,
    val formattedAddress: ImmutableList<String>?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val jobs: ImmutableList<JobUi>
) {
    companion object {
        val fake = PartnerItemUi(
            id = "random-id",
            name = "WeLoveDevs",
            description = "WeLoveDevs open their doors to the company where you'll be the most happier!",
            logoUrl = "https://devfest-2021-908e1.web.app/img/sponsors/welovedevs.png",
            siteUrl = "https://welovedevs.com/",
            twitterUrl = "https://twitter.com/welovedevs",
            twitterMessage = "Depuis plusieurs années, @WeLoveDevs est partenaire Silver du #DevfestLille !\n\n" +
                "Si vous n'allez pas les voir, ce sont eux qui risque de venir vous chercher avec leur micro \uD83C\uDFA4 \n\n" +
                "Alors rendez-vous le 10 Juin prochain sur leur stand ! \uD83D\uDE80",
            linkedinUrl = "https://www.linkedin.com/company/welovedevs/",
            linkedinMessage = null,
            formattedAddress = persistentListOf("165 Av. de Bretagne", "59000 Lille", "France"),
            address = "165 Av. de Bretagne, 59000 Lille, France",
            latitude = 50.6340034,
            longitude = 3.0207872,
            jobs = persistentListOf(JobUi.fake)
        )
    }
}
