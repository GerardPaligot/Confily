package org.gdglille.devfest.android.theme.m3.speakers.di

import com.paligot.confily.core.di.repositoriesModule
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerDetailViewModel
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val speakersModule = module {
    includes(repositoriesModule)
    viewModel { SpeakersListViewModel(get()) }
    viewModel { parameters -> SpeakerDetailViewModel(parameters.get(), get(), get()) }
}
