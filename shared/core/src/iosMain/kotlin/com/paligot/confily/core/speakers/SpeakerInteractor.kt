package com.paligot.confily.core.speakers

import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.speakers.entities.mapToSpeakerItemUi
import com.paligot.confily.core.speakers.entities.mapToSpeakerUi
import com.paligot.confily.resources.Strings
import com.paligot.confily.speakers.panes.models.SpeakerUi
import com.paligot.confily.speakers.ui.models.SpeakerItemUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpeakerInteractor(
    private val repository: SpeakerRepository,
    private val lyricist: Lyricist<Strings>
) {
    @NativeCoroutines
    fun speaker(speakerId: String): Flow<SpeakerUi> = repository.speaker(speakerId)
        .map { it.mapToSpeakerUi(lyricist.strings) }

    @NativeCoroutines
    fun speakers(): Flow<ImmutableList<SpeakerItemUi>> = repository.speakers()
        .map { speakers -> speakers.map { it.mapToSpeakerItemUi(lyricist.strings) }.toImmutableList() }
}
