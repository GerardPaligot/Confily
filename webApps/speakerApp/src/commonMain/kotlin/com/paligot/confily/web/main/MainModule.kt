package com.paligot.confily.web.main

import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.AlarmSchedulerWasm
import com.paligot.confily.core.di.repositoriesModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    includes(repositoriesModule)
    single<AlarmScheduler> { AlarmSchedulerWasm() }
    viewModel { MainViewModel(get()) }
}
