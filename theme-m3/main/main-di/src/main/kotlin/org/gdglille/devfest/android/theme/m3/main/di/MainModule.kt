package org.gdglille.devfest.android.theme.m3.main.di

import com.paligot.confily.core.repositoriesModule
import org.gdglille.devfest.android.theme.MainNavigationViewModel
import org.gdglille.devfest.android.theme.MainViewModel
import org.gdglille.devfest.android.theme.m3.events.di.eventListModule
import org.gdglille.devfest.android.theme.m3.infos.di.infosModule
import org.gdglille.devfest.android.theme.m3.networking.di.networkingModule
import org.gdglille.devfest.android.theme.m3.partners.di.partnersModule
import org.gdglille.devfest.android.theme.m3.schedules.di.scheduleModule
import org.gdglille.devfest.android.theme.m3.speakers.di.speakersModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    includes(
        repositoriesModule,
        eventListModule,
        infosModule,
        networkingModule,
        partnersModule,
        scheduleModule,
        speakersModule
    )
    viewModel { MainNavigationViewModel(get(), get()) }
    viewModel { parameters -> MainViewModel(parameters.get(), get()) }
}
