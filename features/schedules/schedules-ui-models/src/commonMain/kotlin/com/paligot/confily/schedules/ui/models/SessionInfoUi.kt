package com.paligot.confily.schedules.ui.models

data class SessionInfoUi(
    val title: String,
    val slotTime: String,
    val timeInMinutes: Int,
    val room: String,
    val level: String?,
    val category: CategoryUi
) {
    companion object {
        val fake = SessionInfoUi(
            title = "L’intelligence artificielle au secours de l’accessibilité ",
            slotTime = "10:00",
            timeInMinutes = 60,
            room = "Stage 1",
            level = "Beginner",
            category = CategoryUi(id = "", name = "Web", color = "default", icon = "default")
        )
    }
}
