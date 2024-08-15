package org.gdglille.devfest.android.core.sample

import org.gdglille.devfest.ApplicationIdNamed
import org.gdglille.devfest.Conference4HallBaseUrlNamed
import org.gdglille.devfest.IsDebugNamed
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { BuildConfig.DEBUG }
    single(named(ApplicationIdNamed)) { "org.gdglille.devfest.android" }
    single(named(Conference4HallBaseUrlNamed)) { BuildConfig.BASE_URL }
}
