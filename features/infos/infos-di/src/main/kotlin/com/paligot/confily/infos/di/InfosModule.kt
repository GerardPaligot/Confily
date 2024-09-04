package com.paligot.confily.infos.di

import com.paligot.confily.core.di.repositoriesModule
import com.paligot.confily.infos.presentation.CoCViewModel
import com.paligot.confily.infos.presentation.EventViewModel
import com.paligot.confily.infos.presentation.InfoViewModel
import com.paligot.confily.infos.presentation.MenusViewModel
import com.paligot.confily.infos.presentation.QAndAListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val infosModule = module {
    includes(repositoriesModule)
    viewModel { CoCViewModel(get()) }
    viewModel { EventViewModel(get()) }
    viewModel { InfoViewModel(get(), get()) }
    viewModel { MenusViewModel(get()) }
    viewModel { QAndAListViewModel(get()) }
}
