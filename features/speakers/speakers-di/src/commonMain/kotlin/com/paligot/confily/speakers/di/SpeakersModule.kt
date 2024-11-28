package com.paligot.confily.speakers.di

import com.paligot.confily.core.di.repositoriesModule
import com.paligot.confily.speakers.presentation.SpeakerDetailViewModel
import com.paligot.confily.speakers.presentation.SpeakersListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val speakersModule = module {
    includes(repositoriesModule)
    viewModel { SpeakersListViewModel(get(), get()) }
    viewModel { parameters -> SpeakerDetailViewModel(parameters.get(), get(), get(), get()) }
}
