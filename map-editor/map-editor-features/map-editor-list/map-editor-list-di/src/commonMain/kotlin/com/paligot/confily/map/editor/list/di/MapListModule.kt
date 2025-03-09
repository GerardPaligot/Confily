package com.paligot.confily.map.editor.list.di

import com.paligot.confily.core.di.networksModule
import com.paligot.confily.map.editor.list.presentation.MapListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mapListModule = module {
    includes(networksModule)
    viewModelOf(::MapListViewModel)
}
