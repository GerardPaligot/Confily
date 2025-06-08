package com.paligot.confily.backend.talks

import com.paligot.confily.backend.formats.FormatModule.formatDao
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.driveDataSource
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao

object TalkModule {
    val talkRepository = lazy {
        TalkRepository(
            eventFirestore.value,
            speakerDao.value,
            sessionDao.value,
            categoryFirestore.value,
            formatDao.value,
            driveDataSource.value
        )
    }
}
