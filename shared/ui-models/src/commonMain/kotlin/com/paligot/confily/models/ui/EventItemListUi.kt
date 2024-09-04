package com.paligot.confily.models.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class EventItemListUi(
    val future: ImmutableList<com.paligot.confily.models.ui.EventItemUi>,
    val past: ImmutableList<com.paligot.confily.models.ui.EventItemUi>
) {
    companion object {
        val fake = EventItemListUi(
            future = persistentListOf(com.paligot.confily.models.ui.EventItemUi.fake),
            past = persistentListOf(com.paligot.confily.models.ui.EventItemUi.fake)
        )
    }
}
