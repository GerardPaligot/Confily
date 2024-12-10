package com.paligot.confily.core.sample

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.paligot.confily.core.AlarmIntentFactory
import com.paligot.confily.core.AlarmScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val sampleModule = module {
    single<AlarmScheduler> {
        AlarmScheduler(
            context = androidContext(),
            repository = get(),
            alarmManager = androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            alarmIntentFactory = object : AlarmIntentFactory {
                override fun create(
                    context: Context,
                    id: String,
                    title: String,
                    text: String
                ): Intent {
                    return Intent()
                }
            }
        )
    }
    worker { ScheduleWorkManager(androidContext(), get(), get()) }
}
