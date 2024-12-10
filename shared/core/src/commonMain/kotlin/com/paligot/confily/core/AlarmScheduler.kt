package com.paligot.confily.core

import com.paligot.confily.models.ui.TalkItemUi

expect class AlarmScheduler {
    suspend fun schedule(talkItem: TalkItemUi)
}
