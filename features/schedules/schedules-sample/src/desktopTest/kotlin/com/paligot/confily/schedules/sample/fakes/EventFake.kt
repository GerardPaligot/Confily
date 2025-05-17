package com.paligot.confily.schedules.sample.fakes

import com.paligot.confily.BuildKonfig
import com.paligot.confily.core.models.factory.builder
import com.paligot.confily.models.ExportEvent
import kotlinx.datetime.Clock
import kotlin.time.Duration

object EventFake {
    val event = ExportEvent.builder()
        .id(BuildKonfig.DEFAULT_EVENT)
        .startDate(Clock.System.now())
        .endDate(Clock.System.now().plus(Duration.parse("1d")))
        .build()
}
