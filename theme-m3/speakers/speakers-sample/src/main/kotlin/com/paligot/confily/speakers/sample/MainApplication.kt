package com.paligot.confily.speakers.sample

import com.paligot.confily.core.sample.SampleApplication
import com.paligot.confily.core.sample.buildConfigModule
import com.paligot.confily.core.sample.sampleModule
import com.paligot.confily.speakers.di.speakersModule

class MainApplication : SampleApplication(
    koinModules = listOf(buildConfigModule, sampleModule, speakersModule)
)
