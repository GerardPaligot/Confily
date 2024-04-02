package org.gdglille.devfest

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import org.gdglille.devfest.database.DatabaseWrapper
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.repositories.QrCodeGenerator
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults.Companion.standardUserDefaults
import platform.Foundation.preferredLanguages

@OptIn(ExperimentalSettingsApi::class)
actual val platformModule = module {
    single<Conferences4HallDatabase> { DatabaseWrapper().createDb() }
    single<Platform> { Platform(PlatformContext()) }
    single<ObservableSettings> { AppleSettings(standardUserDefaults) }
    single<String>(named(AcceptLanguageNamed)) { NSLocale.preferredLanguages.first().toString() }
    single<QrCodeGenerator> { QrCodeGeneratoriOS() }
}
