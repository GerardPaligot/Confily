package com.paligot.confily.core.di

import com.paligot.confily.core.Platform
import com.paligot.confily.core.PlatformContext
import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.QrCodeGeneratoriOS
import com.paligot.confily.core.db.DatabaseWrapper
import com.paligot.confily.db.ConfilyDatabase
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults.Companion.standardUserDefaults
import platform.Foundation.preferredLanguages

@OptIn(ExperimentalSettingsApi::class)
actual val platformModule = module {
    single<ConfilyDatabase> { DatabaseWrapper().createDb() }
    single<Platform> { Platform(PlatformContext()) }
    single<ObservableSettings> { AppleSettings(standardUserDefaults) }
    single<String>(named(AcceptLanguageNamed)) { NSLocale.preferredLanguages.first().toString() }
    single<QrCodeGenerator> { QrCodeGeneratoriOS() }
}
