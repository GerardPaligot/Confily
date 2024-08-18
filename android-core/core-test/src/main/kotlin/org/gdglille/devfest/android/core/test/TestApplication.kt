package org.gdglille.devfest.android.core.test

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import org.koin.core.component.KoinComponent

class TestApplication : Application(), KoinComponent, Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
}
