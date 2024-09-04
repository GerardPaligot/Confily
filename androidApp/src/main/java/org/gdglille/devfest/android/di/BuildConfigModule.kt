package org.gdglille.devfest.android.di

import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.Conference4HallBaseUrlNamed
import com.paligot.confily.core.di.IsDebugNamed
import org.gdglille.devfest.android.BuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { BuildConfig.DEBUG }
    single(named(ApplicationIdNamed)) { BuildConfig.APPLICATION_ID }
    single(named(Conference4HallBaseUrlNamed)) { BuildConfig.BASE_URL }
}
