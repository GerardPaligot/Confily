package com.paligot.confily.android.di

import android.app.AlarmManager
import android.content.Context
import com.paligot.confily.android.AlarmIntentFactoryImpl
import com.paligot.confily.android.ScheduleWorkManager
import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.main.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {
    includes(buildConfigModule, mainModule)
    single {
        AlarmScheduler(
            get(),
            androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            AlarmIntentFactoryImpl
        )
    }
    worker { ScheduleWorkManager(androidContext(), get(), get()) }
}
