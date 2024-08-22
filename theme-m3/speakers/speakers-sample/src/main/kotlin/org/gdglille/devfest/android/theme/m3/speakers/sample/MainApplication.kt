package org.gdglille.devfest.android.theme.m3.speakers.sample

import org.gdglille.devfest.android.core.sample.SampleApplication
import org.gdglille.devfest.android.core.sample.buildConfigModule
import org.gdglille.devfest.android.core.sample.sampleModule
import org.gdglille.devfest.android.theme.m3.speakers.di.speakersModule

class MainApplication : SampleApplication(
    koinModules = listOf(buildConfigModule, sampleModule, speakersModule)
)
