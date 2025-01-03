package com.paligot.confily.core

import com.paligot.confily.schedules.ui.models.TalkItemUi

expect class AlarmScheduler {
    suspend fun schedule(talkItem: TalkItemUi)
}
