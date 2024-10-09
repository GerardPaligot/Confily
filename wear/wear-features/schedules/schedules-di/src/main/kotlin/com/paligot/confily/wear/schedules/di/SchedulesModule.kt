package com.paligot.confily.wear.schedules.di

import com.paligot.confily.wear.schedules.presentation.ScheduleViewModel
import com.paligot.confily.wear.schedules.presentation.SchedulesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val schedulesModule = module {
    viewModel { SchedulesViewModel(get()) }
    viewModel { params -> ScheduleViewModel(sessionId = params.get(), repository = get()) }
}
