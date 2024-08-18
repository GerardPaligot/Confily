package org.gdglille.devfest.android.core.test

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.gdglille.devfest.AlarmIntentFactory
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.ApplicationIdNamed
import org.gdglille.devfest.Conference4HallBaseUrlNamed
import org.gdglille.devfest.IsDebugNamed
import org.gdglille.devfest.android.core.sample.ScheduleWorkManager
import org.gdglille.devfest.database.DatabaseWrapper
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

internal object AlarmIntentFactoryFake: AlarmIntentFactory {
    override fun create(context: Context, id: String, title: String, text: String): Intent {
        return Intent()
    }
}

val instrumentedModule = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "org.gdglille.devfest.android.core.test" }
    single(named(Conference4HallBaseUrlNamed)) { "" }
    single {
        AlarmScheduler(
            get(),
            androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            AlarmIntentFactoryFake
        )
    }
    single<CoroutineContext> { UnconfinedTestDispatcher() }
    single<Conferences4HallDatabase> { DatabaseWrapper(androidContext(), null).createDb() }
    worker { ScheduleWorkManager(androidContext(), get(), get()) }
}
