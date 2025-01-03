package com.paligot.confily.schedules.panes.models

import com.paligot.confily.schedules.ui.models.SessionInfoUi
import com.paligot.confily.schedules.ui.models.SpeakerItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SessionUi(
    val info: SessionInfoUi,
    val abstract: String,
    val speakers: ImmutableList<SpeakerItemUi>,
    val speakersSharing: String,
    val canGiveFeedback: Boolean,
    val openFeedbackProjectId: String?,
    val openFeedbackSessionId: String?,
    val openFeedbackUrl: String?
) {
    companion object {
        val fake = SessionUi(
            info = SessionInfoUi.fake,
            abstract = "Votre logiciel hang, vous ne savez pas pourquoi ? Ou votre application préférée ne\\nlit pas sa configuration et vous ne savez pas pourquoi ?\\n\\nIl existe beaucoup d'outils fournis avec Linux. Pourtant beaucoup de développeurs\\nne les connaissent pas ou ne les utilisent pas.\\n\\nA travers une série de cas d'utilisation, nous verrons comment utiliser tout ces\\noutils: grep, find, xargs, strace, tcpdump, lsof",
            speakers = persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
            speakersSharing = "",
            canGiveFeedback = false,
            openFeedbackProjectId = null,
            openFeedbackSessionId = "e1Wt4hveV3UiEd5yxZ9k",
            openFeedbackUrl = "https://openfeedback.io/eimghD4rG79eQuZRu2oT/2022-06-10/e1Wt4hveV3UiEd5yxZ9k"
        )
    }
}
