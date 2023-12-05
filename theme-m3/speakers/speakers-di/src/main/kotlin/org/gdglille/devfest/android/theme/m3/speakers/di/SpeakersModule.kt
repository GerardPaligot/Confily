package org.gdglille.devfest.android.theme.m3.speakers.di

import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerDetailViewModel
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersListViewModel
import org.gdglille.devfest.repositoriesModule
import org.koin.dsl.module

val speakersModule = module {
    includes(repositoriesModule)
    single { SpeakersListViewModel(get()) }
    single { parameters -> SpeakerDetailViewModel(parameters.get(), get(), get()) }
}
