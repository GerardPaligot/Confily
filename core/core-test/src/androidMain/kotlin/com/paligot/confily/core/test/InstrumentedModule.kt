package com.paligot.confily.core.test

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.paligot.confily.core.AlarmIntentFactory
import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.db.DatabaseWrapper
import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.IsDebugNamed
import com.paligot.confily.core.sample.ScheduleWorkManager
import com.paligot.confily.db.ConfilyDatabase
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

actual val instrumentedModule = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "com.paligot.confily.core.test" }
    single(named(ConfilyBaseUrlNamed)) { "" }
    single<AlarmScheduler> {
        AlarmScheduler(
            androidContext(),
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
    single<ConfilyDatabase> { DatabaseWrapper(androidContext(), null).createDb() }
    worker { ScheduleWorkManager(androidContext(), get(), get()) }
}
