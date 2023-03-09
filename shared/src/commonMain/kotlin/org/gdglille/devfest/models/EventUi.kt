package org.gdglille.devfest.models

data class EventUi(
    val eventInfo: EventInfoUi,
    val ticket: TicketUi?
) {
    companion object {
        val fake = EventUi(
            eventInfo = EventInfoUi.fake,
            ticket = TicketUi.fake
        )
    }
}
