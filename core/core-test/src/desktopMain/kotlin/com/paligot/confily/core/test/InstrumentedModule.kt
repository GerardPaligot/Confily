package com.paligot.confily.core.test

import com.paligot.confily.core.db.DatabaseWrapper
import com.paligot.confily.db.ConfilyDatabase
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

actual val instrumentedModule = module {
    single<CoroutineContext> { UnconfinedTestDispatcher() }
    single<ConfilyDatabase> { DatabaseWrapper().createDb(inMemory = true) }
    single<ObservableSettings> { MapSettings() }
}
