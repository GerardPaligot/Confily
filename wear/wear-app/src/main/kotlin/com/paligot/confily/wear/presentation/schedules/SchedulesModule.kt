package com.paligot.confily.wear.presentation.schedules

import com.paligot.confily.wear.presentation.schedules.presentation.ScheduleViewModel
import com.paligot.confily.wear.presentation.schedules.presentation.SchedulesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val schedulesModule = module {
    viewModel { SchedulesViewModel(get()) }
    viewModel { params -> ScheduleViewModel(sessionId = params.get(), repository = get()) }
}
