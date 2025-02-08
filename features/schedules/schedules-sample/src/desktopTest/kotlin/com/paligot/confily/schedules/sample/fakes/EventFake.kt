package com.paligot.confily.schedules.sample.fakes

import com.paligot.confily.core.models.factory.builder
import com.paligot.confily.models.EventV4
import kotlinx.datetime.Clock
import kotlin.time.Duration

object EventFake {
    val event = EventV4.builder()
        .id("droidcon-london")
        .startDate(Clock.System.now())
        .endDate(Clock.System.now().plus(Duration.parse("1d")))
        .build()
}
