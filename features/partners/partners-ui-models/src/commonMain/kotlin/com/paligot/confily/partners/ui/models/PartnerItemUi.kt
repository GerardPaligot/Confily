package com.paligot.confily.partners.ui.models

data class PartnerItemUi(
    val id: String,
    val name: String,
    val logoUrl: String
) {
    companion object {
        val fake = PartnerItemUi(
            id = "random-id",
            name = "WeLoveDevs",
            logoUrl = "https://devfest-2021-908e1.web.app/img/sponsors/welovedevs.png"
        )
    }
}
