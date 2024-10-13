package com.paligot.confily.core

import com.paligot.confily.models.ui.TalkItemUi

class AlarmSchedulerWasm : AlarmScheduler {
    override suspend fun schedule(talkItem: TalkItemUi) {
        /* no-op */
    }
}
