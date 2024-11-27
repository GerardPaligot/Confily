package com.paligot.confily.wear.schedules.di

import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.resources.Strings
import com.paligot.confily.wear.schedules.presentation.ScheduleViewModel
import com.paligot.confily.wear.schedules.presentation.SchedulesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val schedulesModule = module {
    viewModel { SchedulesViewModel(get()) }
    viewModel { params ->
        ScheduleViewModel(
            sessionId = params.get(),
            repository = get(),
            strings = get<Lyricist<Strings>>().strings
        )
    }
}
