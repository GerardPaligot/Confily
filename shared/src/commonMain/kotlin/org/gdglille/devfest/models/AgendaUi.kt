package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class AgendaUi(
    val onlyFavorites: Boolean,
    val talks: ImmutableMap<String, ImmutableList<TalkItemUi>>
) {
    companion object {
        val fake = AgendaUi(
            onlyFavorites = false,
            talks = persistentMapOf(
                "10:00" to persistentListOf(TalkItemUi.fake, TalkItemUi.fake),
                "11:00" to persistentListOf(TalkItemUi.fake, TalkItemUi.fake),
                "12:00" to persistentListOf(TalkItemUi.fakePause),
                "13:00" to persistentListOf(TalkItemUi.fake, TalkItemUi.fake),
            )
        )
    }
}
