package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class EventItemListUi(
    val future: ImmutableList<EventItemUi>,
    val past: ImmutableList<EventItemUi>
) {
    companion object {
        val fake = EventItemListUi(
            future = persistentListOf(EventItemUi.fake),
            past = persistentListOf(EventItemUi.fake)
        )
    }
}
