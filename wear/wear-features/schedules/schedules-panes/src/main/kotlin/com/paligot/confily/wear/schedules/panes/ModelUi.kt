package com.paligot.confily.wear.schedules.panes

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.SpeakerItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@Immutable
data class ScheduleModelUi(
    val sessions: ImmutableMap<String, ImmutableList<SessionModelUi>>
)

interface SessionModelUi

@Immutable
data class EventSessionModelUi(
    val title: String,
    val timeSlot: String,
    val timeDuration: String
) : SessionModelUi

@Stable
@Immutable
data class ScheduleSessionModelUi(
    val id: String,
    val title: String,
    val timeSlot: String,
    val timeDuration: String,
    val categoryUi: CategoryUi
) : SessionModelUi

@Stable
@Immutable
data class SessionDetailModelUi(
    val title: String,
    val slotTime: String,
    val room: String,
    val abstract: String,
    val speakers: ImmutableList<SpeakerItemUi>
)
