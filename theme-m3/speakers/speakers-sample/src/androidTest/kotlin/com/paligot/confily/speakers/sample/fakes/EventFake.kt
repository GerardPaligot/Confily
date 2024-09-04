package com.paligot.confily.speakers.sample.fakes

import com.paligot.confily.core.models.factory.builder
import com.paligot.confily.core.sample.BuildConfig
import com.paligot.confily.models.EventV3
import kotlinx.datetime.Clock
import kotlin.time.Duration

object EventFake {
    val event = EventV3.builder()
        .id(BuildConfig.DEFAULT_EVENT)
        .startDate(Clock.System.now())
        .endDate(Clock.System.now().plus(Duration.parse("1d")))
        .build()
}
