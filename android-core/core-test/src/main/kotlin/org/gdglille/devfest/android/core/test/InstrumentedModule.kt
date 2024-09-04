package org.gdglille.devfest.android.core.test

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.paligot.confily.core.AlarmIntentFactory
import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.database.DatabaseWrapper
import com.paligot.confily.db.Conferences4HallDatabase
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.gdglille.devfest.ApplicationIdNamed
import org.gdglille.devfest.Conference4HallBaseUrlNamed
import org.gdglille.devfest.IsDebugNamed
import org.gdglille.devfest.android.core.sample.ScheduleWorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val instrumentedModule = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "org.gdglille.devfest.android.core.test" }
    single(named(Conference4HallBaseUrlNamed)) { "" }
    single {
        AlarmScheduler(
            get(),
            androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            object : AlarmIntentFactory {
                override fun create(context: Context, id: String, title: String, text: String): Intent {
                    return Intent()
                }
            }
        )
    }
    single<CoroutineContext> { UnconfinedTestDispatcher() }
    single<Conferences4HallDatabase> { DatabaseWrapper(androidContext(), null).createDb() }
    worker { ScheduleWorkManager(androidContext(), get(), get()) }
}
