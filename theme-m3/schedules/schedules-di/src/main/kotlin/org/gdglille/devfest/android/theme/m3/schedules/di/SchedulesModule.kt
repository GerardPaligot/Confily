package org.gdglille.devfest.android.theme.m3.schedules.di

import com.paligot.confily.core.di.repositoriesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import com.paligot.confily.schedules.presentation.AgendaFiltersViewModel
import com.paligot.confily.schedules.presentation.ScheduleDetailEventSessionViewModel
import com.paligot.confily.schedules.presentation.ScheduleDetailViewModel
import com.paligot.confily.schedules.presentation.ScheduleGridViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
val scheduleModule = module {
    includes(repositoriesModule)
    viewModel { AgendaFiltersViewModel(get()) }
    viewModel { parameters -> ScheduleDetailViewModel(parameters.get(), get()) }
    viewModel { parameters -> ScheduleDetailEventSessionViewModel(parameters.get(), get()) }
    viewModel { ScheduleGridViewModel(get(), get()) }
}
