package com.paligot.confily.partners.ui.models

import com.paligot.confily.socials.ui.models.SocialUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PartnerUi(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val formattedAddress: ImmutableList<String>?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val socials: ImmutableList<SocialUi>,
    val jobs: ImmutableList<JobUi>
) {
    companion object {
        val fake = PartnerUi(
            id = "random-id",
            name = "WeLoveDevs",
            description = "WeLoveDevs open their doors to the company where you'll be the most happier!",
            logoUrl = "https://devfest-2021-908e1.web.app/img/sponsors/welovedevs.png",
            formattedAddress = persistentListOf("165 Av. de Bretagne", "59000 Lille", "France"),
            address = "165 Av. de Bretagne, 59000 Lille, France",
            latitude = 50.6340034,
            longitude = 3.0207872,
            socials = persistentListOf(SocialUi.x, SocialUi.linkedIn),
            jobs = persistentListOf(JobUi.fake)
        )
    }
}
