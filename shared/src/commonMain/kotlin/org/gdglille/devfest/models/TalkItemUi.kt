package org.gdglille.devfest.models

data class TalkItemUi(
    val id: String,
    val slotTime: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val title: String,
    val speakers: List<String>,
    val isFavorite: Boolean
) {
    val isPause: Boolean = id.contains("-pause")
    companion object {
        val fake = TalkItemUi(
            id = "7TTU2GNH3fOu76Q5MrxNkV3ry7l1",
            slotTime = "10:00",
            startTime = "2022-06-10T09:00:00.000",
            endTime = "2022-06-10T09:50:00.000",
            room = "Salle 700",
            title = "L’intelligence artificielle au secours de l’accessibilité",
            speakers = arrayListOf("Guillaume Laforge", "Aurélie Vache"),
            isFavorite = false
        )
        val fakePause = TalkItemUi(
            id = "12:00-pause",
            slotTime = "12:00",
            startTime = "2022-06-10T10:50:00.000",
            endTime = "2022-06-10T11:10:00.000",
            room = "Salle Lumière",
            title = "Pause",
            speakers = emptyList(),
            isFavorite = false
        )
    }
}
