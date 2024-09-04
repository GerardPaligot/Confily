package org.gdglille.devfest.android.theme.m3.schedules.di

import com.paligot.confily.core.di.repositoriesModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.theme.m3.schedules.feature.AgendaFiltersViewModel
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleDetailEventSessionViewModel
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleDetailViewModel
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleGridViewModel
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
