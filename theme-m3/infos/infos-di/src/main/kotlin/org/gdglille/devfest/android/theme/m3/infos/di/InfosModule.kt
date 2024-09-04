package org.gdglille.devfest.android.theme.m3.infos.di

import com.paligot.confily.core.repositoriesModule
import org.gdglille.devfest.android.theme.m3.infos.feature.CoCViewModel
import org.gdglille.devfest.android.theme.m3.infos.feature.EventViewModel
import org.gdglille.devfest.android.theme.m3.infos.feature.InfoViewModel
import org.gdglille.devfest.android.theme.m3.infos.feature.MenusViewModel
import org.gdglille.devfest.android.theme.m3.infos.feature.QAndAListViewModel
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
