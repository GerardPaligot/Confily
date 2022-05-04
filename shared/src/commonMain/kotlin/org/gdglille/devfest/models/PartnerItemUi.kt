package org.gdglille.devfest.models

data class PartnerItemUi(
    val logoUrl: String,
    val siteUrl: String?,
    val name: String
) {
    companion object {
        val fake = PartnerItemUi(
            logoUrl = "https://firebasestorage.googleapis.com/v0/b/cms4partners-ce427.appspot.com/o/logo%2Fxm6p87HGaZEU5MxuAgTe?alt=media&token=9689d847-ee5a-4edd-a9dc-bfa03af773be",
            siteUrl = "https://sfeir.com",
            name = "SFEIR"
        )
    }
}
