package com.paligot.confily.core

import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.schedules.ui.models.TalkItemUi

actual class AlarmScheduler(
    private val repository: SessionRepository
) {
    actual suspend fun schedule(talkItem: TalkItemUi) {
        repository.markAsRead(talkItem.id, !talkItem.isFavorite)
    }
}
