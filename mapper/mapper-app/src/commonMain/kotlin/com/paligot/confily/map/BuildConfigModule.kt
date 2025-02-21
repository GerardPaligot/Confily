package com.paligot.confily.map

import com.paligot.confily.core.di.AcceptLanguageNamed
import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.IsDebugNamed
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "com.paligot.confily.mapper" }
    single(named(ConfilyBaseUrlNamed)) { "https://confily-619943890215.europe-west1.run.app" }
    single(named(AcceptLanguageNamed)) { "fr" }
}
