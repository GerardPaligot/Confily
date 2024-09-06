package com.paligot.confily.android

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paligot.confily.android.widgets.AgendaAppWidget
import com.paligot.confily.core.repositories.AgendaRepository
import org.koin.core.component.KoinComponent

class ScheduleWorkManager(
    private val context: Context,
    parameters: WorkerParameters,
    private val repository: AgendaRepository
) : CoroutineWorker(context, parameters), KoinComponent {
    override suspend fun doWork(): Result {
        return try {
            repository.fetchAndStoreAgenda()
            AgendaAppWidget().updateAll(context = context)
            Result.success()
        } catch (_: Throwable) {
            Result.failure()
        }
    }
}
