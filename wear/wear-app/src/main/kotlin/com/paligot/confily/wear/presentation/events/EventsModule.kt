package com.paligot.confily.wear.presentation.events

import com.paligot.confily.wear.presentation.events.presentation.CoCViewModel
import com.paligot.confily.wear.presentation.events.presentation.EventViewModel
import com.paligot.confily.wear.presentation.events.presentation.ListEventViewModel
import com.paligot.confily.wear.presentation.events.presentation.MenusViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {
    viewModel { ListEventViewModel(repository = get()) }
    viewModel { EventViewModel(agendaRepository = get(), eventRepository = get()) }
    viewModel { MenusViewModel(repository = get()) }
    viewModel { CoCViewModel(repository = get()) }
}
