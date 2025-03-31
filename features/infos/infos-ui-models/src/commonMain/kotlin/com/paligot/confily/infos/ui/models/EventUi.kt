package com.paligot.confily.infos.ui.models

data class EventUi(
    val eventInfo: EventInfoUi,
    val ticket: TicketUi?,
    val versionCode: String
) {
    companion object {
        val fake = EventUi(
            eventInfo = EventInfoUi.fake,
            ticket = TicketUi.fake,
            versionCode = "1.0.0"
        )
    }
}
