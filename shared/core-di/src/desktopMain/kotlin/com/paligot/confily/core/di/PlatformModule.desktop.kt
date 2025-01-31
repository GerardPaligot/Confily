package com.paligot.confily.core.di

import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.Platform
import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.QrCodeGeneratorJvm
import com.paligot.confily.core.db.DatabaseWrapper
import com.paligot.confily.db.ConfilyDatabase
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import okio.FileSystem
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.Locale
import java.util.prefs.Preferences

actual val platformModule = module {
    single<ConfilyDatabase> { DatabaseWrapper().createDb() }
    single(named(TempFolderPathNamed)) { FileSystem.SYSTEM_TEMPORARY_DIRECTORY }
    single<Platform> { Platform() }
    single<ObservableSettings> { PreferencesSettings(Preferences.userRoot()) }
    single<String>(named(AcceptLanguageNamed)) { Locale.getDefault().toLanguageTag() }
    single<QrCodeGenerator> { QrCodeGeneratorJvm() }
    single<AlarmScheduler> { AlarmScheduler() }
}
