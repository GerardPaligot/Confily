package com.paligot.confily.core.di

import android.content.Context
import com.paligot.confily.core.AndroidContext
import com.paligot.confily.core.Platform
import com.paligot.confily.core.QrCodeGeneratorAndroid
import com.paligot.confily.core.database.DatabaseWrapper
import com.paligot.confily.core.repositories.QrCodeGenerator
import com.paligot.confily.db.Conferences4HallDatabase
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.Locale

@OptIn(ExperimentalSettingsApi::class)
actual val platformModule = module {
    single<Conferences4HallDatabase> { DatabaseWrapper(androidContext(), "confily.db").createDb() }
    single<Platform> { Platform(AndroidContext(androidApplication())) }
    single<ObservableSettings> {
        AndroidSettings(
            androidContext().getSharedPreferences(
                /* name = */
                "com.paligot.confily.android",
                /* mode = */
                Context.MODE_PRIVATE
            )
        )
    }
    single<String>(named(AcceptLanguageNamed)) { Locale.getDefault().toLanguageTag() }
    single<QrCodeGenerator> { QrCodeGeneratorAndroid() }
}
