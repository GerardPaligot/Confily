package com.paligot.confily.models.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class AgendaUi(
    val onlyFavorites: Boolean,
    val sessions: ImmutableMap<String, ImmutableList<com.paligot.confily.models.ui.SessionItemUi>>
) {
    companion object {
        val fake = com.paligot.confily.models.ui.AgendaUi(
            onlyFavorites = false,
            sessions = persistentMapOf(
                "10:00" to persistentListOf(
                    com.paligot.confily.models.ui.TalkItemUi.Companion.fake.copy(id = "1"),
                    com.paligot.confily.models.ui.TalkItemUi.Companion.fake.copy(id = "2")
                ),
                "11:00" to persistentListOf(
                    com.paligot.confily.models.ui.TalkItemUi.Companion.fake.copy(id = "3"),
                    com.paligot.confily.models.ui.TalkItemUi.Companion.fake.copy(id = "4")
                ),
                "12:00" to persistentListOf(com.paligot.confily.models.ui.EventSessionItemUi.Companion.fakePause),
                "13:00" to persistentListOf(
                    com.paligot.confily.models.ui.TalkItemUi.Companion.fake.copy(id = "5"),
                    com.paligot.confily.models.ui.TalkItemUi.Companion.fake.copy(id = "6")
                )
            )
        )
    }
}
