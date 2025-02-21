package com.paligot.confily.mapper.detail.di

import com.paligot.confily.core.di.networksModule
import com.paligot.confily.mapper.detail.presentation.MapDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mapDetailModule = module {
    includes(networksModule)
    viewModelOf(::MapDetailViewModel)
}
