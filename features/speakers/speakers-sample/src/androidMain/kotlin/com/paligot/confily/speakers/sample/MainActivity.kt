package com.paligot.confily.speakers.sample

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalConfiguration
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.paligot.confily.core.sample.ScheduleWorkManager
import org.koin.compose.KoinContext
import org.koin.mp.KoinPlatformTools

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = OneTimeWorkRequestBuilder<ScheduleWorkManager>()
            .setConstraints(constraints)
            .build()
        workManager.enqueue(request)
        setContent {
            val config = LocalConfiguration.current
            // FIXME This is necessary due to a bug in Koin.
            //  The scope isn't well updated between two test cases in the same process.
            //  https://github.com/InsertKoinIO/koin/issues/1844#issuecomment-2295385215
            KoinContext(context = KoinPlatformTools.defaultContext().get()) {
                App(
                    isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE,
                    launchUrl = {}
                )
            }
        }
    }
}
