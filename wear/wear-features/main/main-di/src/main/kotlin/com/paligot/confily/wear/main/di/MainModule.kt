package com.paligot.confily.wear.main.di

import com.paligot.confily.wear.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(get()) }
}
