package org.gdglille.devfest.models.ui

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
