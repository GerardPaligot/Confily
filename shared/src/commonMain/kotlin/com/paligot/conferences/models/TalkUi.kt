package com.paligot.conferences.models

data class TalkUi(
    val title: String,
    val date: String,
    val room: String,
    val level: String?,
    val abstract: String,
    val speakers: List<SpeakerItemUi>
) {
    companion object {
        val fake = TalkUi(
            title = "L’intelligence artificielle au secours de l’accessibilité ",
            date = "12:00",
            room = "Stage 1",
            level = "Beginner",
            abstract = "Votre logiciel hang, vous ne savez pas pourquoi ? Ou votre application préférée ne\\nlit pas sa configuration et vous ne savez pas pourquoi ?\\n\\nIl existe beaucoup d'outils fournis avec Linux. Pourtant beaucoup de développeurs\\nne les connaissent pas ou ne les utilisent pas.\\n\\nA travers une série de cas d'utilisation, nous verrons comment utiliser tout ces\\noutils: grep, find, xargs, strace, tcpdump, lsof",
            speakers = arrayListOf(SpeakerItemUi.fake, SpeakerItemUi.fake)
        )
    }
}
