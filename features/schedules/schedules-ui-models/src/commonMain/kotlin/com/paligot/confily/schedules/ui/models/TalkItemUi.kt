package com.paligot.confily.schedules.ui.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed class SessionItemUi {
    abstract val id: String
    abstract val order: Int
    abstract val slotTime: String
}

data class TalkItemUi(
    override val id: String,
    override val order: Int,
    override val slotTime: String,
    val startTime: String,
    val timeInMinutes: Int,
    val time: String,
    val room: String,
    val level: String?,
    val title: String,
    val category: CategoryUi,
    val speakers: ImmutableList<String>,
    val speakersAvatar: ImmutableList<String>,
    val speakersLabel: String,
    val isFavorite: Boolean
) : SessionItemUi() {
    companion object {
        val fake = TalkItemUi(
            id = "7TTU2GNH3fOu76Q5MrxNkV3ry7l1",
            order = 0,
            slotTime = "10:00",
            startTime = "2022-06-10T09:00:00.000",
            timeInMinutes = 50,
            time = "50 minutes",
            room = "Salle 700",
            level = "beginner",
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            category = CategoryUi(id = "", name = "UI/UX", color = "emerald", icon = "default"),
            speakers = persistentListOf("Sabrina VIGIL", "Simon Leclercq"),
            speakersAvatar = persistentListOf(
                "https://storage.googleapis.com/conferences4hall/2022/speakers/0YOIreL1mbP7tKcTfX5TSaUI6VN2.png",
                "https://storage.googleapis.com/conferences4hall/2022/speakers/w9Lt3a83WkPU1duFnlNxDZcxfsn2.png"
            ),
            speakersLabel = "Sabrina and Simon",
            isFavorite = false
        )
    }
}

data class EventSessionItemUi(
    override val id: String,
    override val order: Int,
    override val slotTime: String,
    val title: String,
    val timeInMinutes: Int,
    val time: String,
    val room: String,
    val isClickable: Boolean
) : SessionItemUi() {
    companion object {
        val fakePause = EventSessionItemUi(
            id = "12:00-pause",
            order = 0,
            slotTime = "12:00",
            timeInMinutes = 20,
            time = "20 minutes",
            room = "Salle Lumière",
            title = "Pause ☕️",
            isClickable = false
        )
    }
}
