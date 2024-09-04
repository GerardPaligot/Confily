package org.gdglille.devfest.android.theme.m3.schedules.sample

import com.paligot.confily.core.sample.SampleApplication
import com.paligot.confily.core.sample.buildConfigModule
import com.paligot.confily.core.sample.sampleModule
import com.paligot.confily.schedules.di.scheduleModule

class MainApplication : SampleApplication(
    koinModules = listOf(buildConfigModule, sampleModule, scheduleModule)
)
