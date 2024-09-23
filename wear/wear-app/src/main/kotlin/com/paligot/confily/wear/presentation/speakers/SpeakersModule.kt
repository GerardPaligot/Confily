package com.paligot.confily.wear.presentation.speakers

import com.paligot.confily.wear.presentation.speakers.presentation.SpeakerViewModel
import com.paligot.confily.wear.presentation.speakers.presentation.SpeakersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val speakersModule = module {
    viewModel { SpeakersViewModel(repository = get()) }
    viewModel { params -> SpeakerViewModel(speakerId = params.get(), repository = get()) }
}
