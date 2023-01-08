package org.gdglille.devfest.models

data class EventItemListUi(
    val future: List<EventItemUi>
) {
    companion object {
        val fake = EventItemListUi(
            future = listOf(
                EventItemUi.fake,
                EventItemUi.fake
            )
        )
    }
}
