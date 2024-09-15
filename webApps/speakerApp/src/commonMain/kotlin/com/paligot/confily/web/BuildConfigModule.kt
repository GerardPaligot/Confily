package com.paligot.confily.web

import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.IsDebugNamed
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "com.paligot.confily.web" }
    single(named(ConfilyBaseUrlNamed)) { "https://api.devlille.fr" }
}
