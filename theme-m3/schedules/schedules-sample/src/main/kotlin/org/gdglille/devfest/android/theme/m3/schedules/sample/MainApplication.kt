package org.gdglille.devfest.android.theme.m3.schedules.sample

import org.gdglille.devfest.android.core.sample.SampleApplication
import org.gdglille.devfest.android.core.sample.buildConfigModule
import org.gdglille.devfest.android.core.sample.sampleModule
import org.gdglille.devfest.android.theme.m3.schedules.di.scheduleModule

class MainApplication : SampleApplication(
    koinModules = listOf(buildConfigModule, sampleModule, scheduleModule)
)
