package com.paligot.confily.core.di

import com.paligot.confily.core.Platform
import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.QrCodeGeneratorWasm
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.observable.makeObservable
import okio.FileSystem
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun navigatorLanguage(): JsAny = js("navigator.language || navigator.userLanguage")

actual val platformModule: Module = module {
    single(named(TempFolderPathNamed)) { FileSystem.SYSTEM_TEMPORARY_DIRECTORY }
    single<Platform> { Platform() }
    single<ObservableSettings> { StorageSettings().makeObservable() }
    single<String>(named(AcceptLanguageNamed)) {
        navigatorLanguage().unsafeCast<JsString>().toString()
    }
    single<QrCodeGenerator> { QrCodeGeneratorWasm() }
}
