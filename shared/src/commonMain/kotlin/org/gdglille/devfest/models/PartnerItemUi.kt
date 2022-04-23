package org.gdglille.devfest.models

data class PartnerItemUi(
    val logoUrl: String,
    val siteUrl: String?,
    val name: String
) {
    companion object {
        val fake = PartnerItemUi(
            logoUrl = "https://pbs.twimg.com/profile_images/1483539472574816261/mi3QaL7u_400x400.png",
            siteUrl = "https://devfest.gdglille.org/",
            name = "Devfest"
        )
    }
}
