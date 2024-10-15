package com.paligot.confily.core

import com.paligot.confily.models.ui.TalkItemUi

interface AlarmScheduler {
    suspend fun schedule(talkItem: TalkItemUi)
}
