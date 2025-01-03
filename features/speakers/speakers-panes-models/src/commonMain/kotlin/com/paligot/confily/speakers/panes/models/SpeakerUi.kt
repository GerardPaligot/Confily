package com.paligot.confily.speakers.panes.models

import com.paligot.confily.schedules.ui.models.TalkItemUi
import com.paligot.confily.speakers.ui.models.SpeakerInfoUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SpeakerUi(
    val info: SpeakerInfoUi,
    val talks: ImmutableList<TalkItemUi>
) {
    companion object {
        val fake = SpeakerUi(
            info = SpeakerInfoUi.fake,
            talks = persistentListOf(TalkItemUi.fake)
        )
    }
}
