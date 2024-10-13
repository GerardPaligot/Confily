package com.paligot.confily.events.di

import com.paligot.confily.core.di.repositoriesModule
import com.paligot.confily.events.presentation.EventListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val eventListModule = module {
    includes(repositoriesModule)
    viewModel { EventListViewModel(get()) }
}
