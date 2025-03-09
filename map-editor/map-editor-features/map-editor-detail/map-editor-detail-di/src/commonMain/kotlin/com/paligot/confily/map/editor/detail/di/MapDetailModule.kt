package com.paligot.confily.map.editor.detail.di

import com.paligot.confily.core.di.networksModule
import com.paligot.confily.map.editor.detail.presentation.MapDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mapDetailModule = module {
    includes(networksModule)
    viewModelOf(::MapDetailViewModel)
}
