package com.paligot.confily.wear.presentation.speakers.presentation

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class SpeakersModelUi(
    val speakers: ImmutableList<SpeakerModelUi>
)

@Immutable
data class SpeakerModelUi(
    val id: String,
    val name: String,
    val job: String,
    val url: String
)
