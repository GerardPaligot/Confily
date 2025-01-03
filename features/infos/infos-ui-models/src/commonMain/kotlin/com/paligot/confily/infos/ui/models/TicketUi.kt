package com.paligot.confily.infos.ui.models

data class TicketInfoUi(
    val id: String,
    val firstName: String,
    val lastName: String
)

data class TicketUi(
    val info: TicketInfoUi?,
    val qrCode: ByteArray?
) {
    companion object {
        val fake = TicketUi(
            info = TicketInfoUi(
                id = "T264-8780-E519953",
                firstName = "Gérard",
                lastName = "Paligot"
            ),
            qrCode = null
        )
    }
}
