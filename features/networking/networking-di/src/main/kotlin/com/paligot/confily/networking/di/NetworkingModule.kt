package com.paligot.confily.networking.di

import com.paligot.confily.core.di.repositoriesModule
import com.paligot.confily.networking.presentation.ContactsViewModel
import com.paligot.confily.networking.presentation.MyProfileViewModel
import com.paligot.confily.networking.presentation.NetworkingViewModel
import com.paligot.confily.networking.presentation.ProfileInputViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkingModule = module {
    includes(repositoriesModule)
    viewModel { ContactsViewModel(get()) }
    viewModel { MyProfileViewModel(get()) }
    viewModel { NetworkingViewModel(get(), get()) }
    viewModel { ProfileInputViewModel(get()) }
}
