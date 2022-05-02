package org.gdglille.devfest.models

import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.extensions.formatHoursMinutes

data class TalkUi(
    val title: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val level: String?,
    val abstract: String,
    val speakers: List<SpeakerItemUi>,
    val speakersSharing: String,
    val canGiveFeedback: Boolean,
    val openFeedbackSessionId: String?
) {
    companion object {
        val fake = TalkUi(
            title = "L’intelligence artificielle au secours de l’accessibilité ",
            startTime = "2022-06-10T10:00:00.000".toLocalDateTime().formatHoursMinutes(),
            endTime = "2022-06-10T11:00:00.000".toLocalDateTime().formatHoursMinutes(),
            room = "Stage 1",
            level = "Beginner",
            abstract = "Votre logiciel hang, vous ne savez pas pourquoi ? Ou votre application préférée ne\\nlit pas sa configuration et vous ne savez pas pourquoi ?\\n\\nIl existe beaucoup d'outils fournis avec Linux. Pourtant beaucoup de développeurs\\nne les connaissent pas ou ne les utilisent pas.\\n\\nA travers une série de cas d'utilisation, nous verrons comment utiliser tout ces\\noutils: grep, find, xargs, strace, tcpdump, lsof",
            speakers = arrayListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
            speakersSharing = "",
            canGiveFeedback = false,
            openFeedbackSessionId = "e1Wt4hveV3UiEd5yxZ9k"
        )
    }
}
