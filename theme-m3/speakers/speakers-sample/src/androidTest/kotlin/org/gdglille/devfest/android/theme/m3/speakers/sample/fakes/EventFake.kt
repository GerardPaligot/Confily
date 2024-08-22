package org.gdglille.devfest.android.theme.m3.speakers.sample.fakes

import kotlinx.datetime.Clock
import org.gdglille.devfest.android.core.models.factory.builder
import org.gdglille.devfest.android.core.sample.BuildConfig
import org.gdglille.devfest.models.EventV3
import kotlin.time.Duration

object EventFake {
    val event = EventV3.builder()
        .id(BuildConfig.DEFAULT_EVENT)
        .startDate(Clock.System.now())
        .endDate(Clock.System.now().plus(Duration.parse("1d")))
        .build()
}
