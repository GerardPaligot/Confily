package com.paligot.confily.infos.ui.models

data class CoCUi(
    val text: String,
    val phone: String?,
    val email: String?
) {
    companion object {
        val fake = CoCUi(
            text = "Code of conduct",
            phone = "0600000000",
            email = "john@doe.com"
        )
    }
}
