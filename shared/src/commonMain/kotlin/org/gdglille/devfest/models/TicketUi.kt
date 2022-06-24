package org.gdglille.devfest.models

import org.gdglille.devfest.Image

data class TicketInfoUi(
    val id: String,
    val firstName: String,
    val lastName: String
)

data class TicketUi(
    val info: TicketInfoUi?,
    val qrCode: Image?
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
        val fakeLongText = TicketUi(
            info = TicketInfoUi(
                id = "T264-8780-E519953",
                firstName = "Gérard Pierre Martin Adrien",
                lastName = "Paligot",
            ),
            qrCode = null
        )
    }
}
