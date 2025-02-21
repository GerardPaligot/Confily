package com.paligot.confily.mapper.list.di

import com.paligot.confily.core.di.networksModule
import com.paligot.confily.mapper.list.presentation.MapListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mapListModule = module {
    includes(networksModule)
    viewModelOf(::MapListViewModel)
}
