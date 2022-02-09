package com.paligot.conferences.models

data class TalkItemUi(
    val id: String,
    val time: String,
    val room: String,
    val title: String,
    val speakers: List<String>,
    val isFavorite: Boolean
) {
    companion object {
        val fake = TalkItemUi(
            id = "7TTU2GNH3fOu76Q5MrxNkV3ry7l1",
            time = "10:00",
            room = "Salle 700",
            title = "L’intelligence artificielle au secours de l’accessibilité",
            speakers = arrayListOf("Guillaume Laforge", "Aurélie Vache"),
            isFavorite = false
        )
    }
}
