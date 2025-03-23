package com.paligot.confily.core.di

import com.paligot.confily.core.api.ConferenceApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networksModule = module {
    includes(platformModule)
    single {
        ConferenceApi.create(
            baseUrl = get(named(ConfilyBaseUrlNamed)),
            enableNetworkLogs = get(named(IsDebugNamed))
        )
    }
}
