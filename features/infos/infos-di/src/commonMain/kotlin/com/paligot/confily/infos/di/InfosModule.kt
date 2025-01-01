package com.paligot.confily.infos.di

import com.paligot.confily.core.di.repositoriesModule
import com.paligot.confily.infos.presentation.CoCViewModel
import com.paligot.confily.infos.presentation.EventViewModel
import com.paligot.confily.infos.presentation.InfoViewModel
import com.paligot.confily.infos.presentation.MenusViewModel
import com.paligot.confily.infos.presentation.QAndAListViewModel
import com.paligot.confily.infos.presentation.TeamMemberListViewModel
import com.paligot.confily.infos.presentation.TeamMemberViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val infosModule = module {
    includes(repositoriesModule)
    viewModel { CoCViewModel(get()) }
    viewModel { EventViewModel(get()) }
    viewModel { InfoViewModel(get()) }
    viewModel { MenusViewModel(get()) }
    viewModel { QAndAListViewModel(get()) }
    viewModel { TeamMemberListViewModel(get()) }
    viewModelOf(::TeamMemberViewModel)
}
