package org.gdglille.devfest.models

data class EventItemUi(
    val id: String,
    val name: String,
    val date: String
) {
    companion object {
        val fake = EventItemUi(
            id = "devfest-lille-2023",
            name = "Devfest Lille 2023",
            date = "26 Mai 2023"
        )
    }
}
