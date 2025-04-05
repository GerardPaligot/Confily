package com.paligot.confily.core.sample

import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.EventDefaultLanguage
import com.paligot.confily.core.di.IsDebugNamed
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { BuildConfig.DEBUG }
    single(named(ApplicationIdNamed)) { "com.paligot.confily.android" }
    single(named(ConfilyBaseUrlNamed)) { BuildConfig.BASE_URL }
    single(named(EventDefaultLanguage)) { "en" }
}
