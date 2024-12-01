package com.paligot.confily.wear.events.di

import com.paligot.confily.wear.events.presentation.CoCViewModel
import com.paligot.confily.wear.events.presentation.EventViewModel
import com.paligot.confily.wear.events.presentation.ListEventViewModel
import com.paligot.confily.wear.events.presentation.MenusViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {
    viewModel { ListEventViewModel(repository = get()) }
    viewModel { EventViewModel(eventRepository = get()) }
    viewModel { MenusViewModel(repository = get()) }
    viewModel { CoCViewModel(repository = get()) }
}
