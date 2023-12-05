package org.gdglille.devfest.android.theme.m3.events.di

import org.gdglille.devfest.android.theme.m3.events.feature.EventListViewModel
import org.gdglille.devfest.repositoriesModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventListModule = module {
    includes(repositoriesModule)
    viewModel { EventListViewModel(get()) }
}
