package org.gdglille.devfest.models

data class AgendaUi(
    val onlyFavorites: Boolean,
    val talks: Map<String, List<TalkItemUi>>
) {
    companion object {
        val fake = AgendaUi(
            onlyFavorites = false,
            talks = mapOf(
                "10:00" to arrayListOf(TalkItemUi.fake, TalkItemUi.fake),
                "11:00" to arrayListOf(TalkItemUi.fake, TalkItemUi.fake),
                "12:00" to arrayListOf(TalkItemUi.fakePause),
                "13:00" to arrayListOf(TalkItemUi.fake, TalkItemUi.fake),
            ),
        )
    }
}
