package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TalkItemUi(
    val id: String,
    val order: Int,
    val slotTime: String,
    val startTime: String,
    val endTime: String,
    val timeInMinutes: Int,
    val room: String,
    val level: String?,
    val title: String,
    val abstract: String,
    val category: CategoryUi,
    val speakers: ImmutableList<String>,
    val speakersAvatar: ImmutableList<String>,
    val isFavorite: Boolean
) {
    val isPause: Boolean = id.contains("-pause")
    companion object {
        val fake = TalkItemUi(
            id = "7TTU2GNH3fOu76Q5MrxNkV3ry7l1",
            order = 0,
            slotTime = "10:00",
            startTime = "2022-06-10T09:00:00.000",
            endTime = "2022-06-10T09:50:00.000",
            timeInMinutes = 50,
            room = "Salle 700",
            level = "beginner",
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            abstract = "Culture, language, outils... Entre designers & developers, pas toujours simple d’être du même côté du ring ! À l’heure de l’expérience utilisateur et de la scalabilité, Sabrina & Simon du Design System de Decathlon vous attendent aux vestiaires pour débriefer et préparer le prochain match !\\nAnalyse tactique, définition du plan de jeu, vérification de l’équipement. \\nOn chausse. Gong \uD83D\uDD14 ! Et c’est parti !",
            category = CategoryUi(name = "UI/UX", color = "default", icon = "default"),
            speakers = persistentListOf("Sabrina VIGIL", "Simon Leclercq"),
            speakersAvatar = persistentListOf(
                "https://storage.googleapis.com/conferences4hall/2022/speakers/0YOIreL1mbP7tKcTfX5TSaUI6VN2.png",
                "https://storage.googleapis.com/conferences4hall/2022/speakers/w9Lt3a83WkPU1duFnlNxDZcxfsn2.png"
            ),
            isFavorite = false
        )
        val fakePause = TalkItemUi(
            id = "12:00-pause",
            order = 0,
            slotTime = "12:00",
            startTime = "2022-06-10T10:50:00.000",
            endTime = "2022-06-10T11:10:00.000",
            timeInMinutes = 20,
            room = "Salle Lumière",
            level = null,
            title = "Pause ☕️",
            abstract = "",
            category = CategoryUi(name = "", color = null, icon = null),
            speakers = persistentListOf(),
            speakersAvatar = persistentListOf(),
            isFavorite = false
        )
    }
}
