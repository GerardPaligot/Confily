package org.gdglille.devfest.android.di

import android.app.AlarmManager
import androidx.activity.ComponentActivity
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.android.AlarmIntentFactoryImpl
import org.gdglille.devfest.android.ScheduleWorkManager
import org.gdglille.devfest.android.theme.m3.main.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {
    includes(buildConfigModule, mainModule)
    single {
        AlarmScheduler(
            get(),
            androidContext().getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager,
            AlarmIntentFactoryImpl
        )
    }
    worker { ScheduleWorkManager(androidContext(), get(), get()) }
}
