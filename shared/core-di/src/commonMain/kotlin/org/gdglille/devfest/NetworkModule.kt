package org.gdglille.devfest

import org.gdglille.devfest.network.ConferenceApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networksModule = module {
    includes(platformModule)
    single {
        ConferenceApi.create(
            platform = get(),
            baseUrl = get(named(Conference4HallBaseUrlNamed)),
            acceptLanguage = get(named(AcceptLanguageNamed)),
            enableNetworkLogs = get(named(IsDebugNamed))
        )
    }
}
