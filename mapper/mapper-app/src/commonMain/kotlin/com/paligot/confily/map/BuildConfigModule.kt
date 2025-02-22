package com.paligot.confily.map

import com.paligot.confily.core.di.AcceptLanguageNamed
import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.IsDebugNamed
import com.paligot.confily.mapper.BuildKonfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "com.paligot.confily.mapper" }
    single(named(ConfilyBaseUrlNamed)) { BuildKonfig.BASE_URL }
    single(named(AcceptLanguageNamed)) { "fr" }
}
