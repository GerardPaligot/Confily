package org.gdglille.devfest.android.di

import org.gdglille.devfest.ApplicationIdNamed
import org.gdglille.devfest.Conference4HallBaseUrlNamed
import org.gdglille.devfest.IsDebugNamed
import org.gdglille.devfest.android.BuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val buildConfigModule = module {
    single(named(IsDebugNamed)) { BuildConfig.DEBUG }
    single(named(ApplicationIdNamed)) { BuildConfig.APPLICATION_ID }
    single(named(Conference4HallBaseUrlNamed)) { BuildConfig.BASE_URL }
}
