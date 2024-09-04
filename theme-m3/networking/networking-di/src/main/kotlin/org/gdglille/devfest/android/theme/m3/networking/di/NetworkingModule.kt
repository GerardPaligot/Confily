package org.gdglille.devfest.android.theme.m3.networking.di

import com.paligot.confily.core.repositoriesModule
import org.gdglille.devfest.android.theme.m3.networking.feature.ContactsViewModel
import org.gdglille.devfest.android.theme.m3.networking.feature.MyProfileViewModel
import org.gdglille.devfest.android.theme.m3.networking.feature.NetworkingViewModel
import org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkingModule = module {
    includes(repositoriesModule)
    viewModel { ContactsViewModel(get()) }
    viewModel { MyProfileViewModel(get()) }
    viewModel { NetworkingViewModel(get(), get()) }
    viewModel { ProfileInputViewModel(get()) }
}
