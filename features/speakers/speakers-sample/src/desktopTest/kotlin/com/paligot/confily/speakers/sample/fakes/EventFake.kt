package com.paligot.confily.speakers.sample.fakes

import com.paligot.confily.core.models.factory.builder
import com.paligot.confily.models.EventV4
import com.paligot.confily.speakers.sample.BuildKonfig
import kotlinx.datetime.Clock
import kotlin.time.Duration

object EventFake {
    val event = EventV4.builder()
        .id(BuildKonfig.DEFAULT_EVENT)
        .startDate(Clock.System.now())
        .endDate(Clock.System.now().plus(Duration.parse("1d")))
        .build()
}
