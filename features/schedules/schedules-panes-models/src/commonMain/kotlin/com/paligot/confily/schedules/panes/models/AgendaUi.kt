package com.paligot.confily.schedules.panes.models

import com.paligot.confily.schedules.ui.models.EventSessionItemUi
import com.paligot.confily.schedules.ui.models.SessionItemUi
import com.paligot.confily.schedules.ui.models.TalkItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class AgendaUi(
    val onlyFavorites: Boolean,
    val sessions: ImmutableMap<String, ImmutableList<SessionItemUi>>
) {
    companion object {
        val fake = AgendaUi(
            onlyFavorites = false,
            sessions = persistentMapOf(
                "10:00" to persistentListOf(
                    TalkItemUi.fake.copy(id = "1"),
                    TalkItemUi.fake.copy(id = "2")
                ),
                "11:00" to persistentListOf(
                    TalkItemUi.fake.copy(id = "3"),
                    TalkItemUi.fake.copy(id = "4")
                ),
                "12:00" to persistentListOf(EventSessionItemUi.fakePause),
                "13:00" to persistentListOf(
                    TalkItemUi.fake.copy(id = "5"),
                    TalkItemUi.fake.copy(id = "6")
                )
            )
        )
    }
}
