package com.paligot.confily.android.di

import com.paligot.confily.BuildKonfig
import com.paligot.confily.android.BuildConfig
import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.EventDefaultLanguage
import com.paligot.confily.core.di.IsDebugNamed
import com.paligot.confily.core.di.VersionCodeNamed
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { BuildConfig.DEBUG }
    single(named(ApplicationIdNamed)) { BuildKonfig.APP_ID }
    single(named(ConfilyBaseUrlNamed)) { BuildKonfig.BASE_URL }
    single(named(EventDefaultLanguage)) { BuildKonfig.DEFAULT_LANGUAGE }
    single(named(VersionCodeNamed)) { BuildKonfig.VERSION_CODE }
}
